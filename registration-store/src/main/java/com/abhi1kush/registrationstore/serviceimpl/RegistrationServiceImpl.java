package com.abhi1kush.registrationstore.serviceimpl;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.exception.RegistrationSerializationException;
import com.abhi1kush.registrationstore.exception.RegistrationValidationException;
import com.abhi1kush.registrationstore.security.SqlInjectionGuard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	private final RegistrationDao registrationDao;
	private final ObjectMapper objectMapper;
	private final Validator validator;
	private final SqlInjectionGuard sqlInjectionGuard;

	public RegistrationServiceImpl(
			RegistrationDao registrationDao,
			ObjectMapper objectMapper,
			Validator validator,
			SqlInjectionGuard sqlInjectionGuard) {
		this.registrationDao = registrationDao;
		this.objectMapper = objectMapper;
		this.validator = validator;
		this.sqlInjectionGuard = sqlInjectionGuard;
	}

	@Override
	public RegistrationResponse create(RegistrationRequest request) {
		String requestId = UUID.randomUUID().toString();
		validateRequestPolicy(request);
		log.debug("Creating registration requestId={} registrationNo={}", requestId, request.getRegistrationNo());
		String payloadJson;
		try {
			payloadJson = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new RegistrationSerializationException(
					"SERIALIZATION_ERROR",
					"Unable to serialize registration request",
					e);
		}
		RegistrationEntity entity = new RegistrationEntity(
				requestId,
				request.getRegistrationNo(),
				request.getRollNo(),
				request.getUniversity(),
				request.getCollege(),
				request.getSourceSystem(),
				payloadJson,
				Instant.now());
		try {
			registrationDao.insert(entity);
		} catch (RegistrationPersistenceException e) {
			log.warn("Failed to persist registration requestId={} registrationNo={}",
					requestId, request.getRegistrationNo(), e);
			throw e;
		}
		log.info("Registration stored successfully requestId={} registrationNo={}", requestId, request.getRegistrationNo());
		return new RegistrationResponse("1", "", "", requestId);
	}

	private void validateRequestPolicy(RegistrationRequest request) {
		Map<String, String> fieldErrors = validator.validate(request).stream()
				.collect(Collectors.toMap(
						violation -> extractFieldName(violation),
						ConstraintViolation::getMessage,
						(first, second) -> first,
						LinkedHashMap::new));
		if (!fieldErrors.isEmpty()) {
			throw new RegistrationValidationException("VALIDATION_ERROR", "Validation failed", fieldErrors);
		}
		if (sqlInjectionGuard.hasSuspiciousContent(request)) {
			throw new RegistrationValidationException(
					"SECURITY_VALIDATION_ERROR",
					"Suspicious input detected",
					Map.of());
		}
	}

	private static String extractFieldName(ConstraintViolation<RegistrationRequest> violation) {
		String path = violation.getPropertyPath().toString();
		int dot = path.indexOf('.');
		return dot > 0 ? path.substring(0, dot) : path;
	}
}
