package com.abhi1kush.registrationstore.serviceimpl;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.abhi1kush.registrationstore.exception.RegistrationPersistenceException;
import com.abhi1kush.registrationstore.exception.RegistrationSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	private final RegistrationDao registrationDao;
	private final ObjectMapper objectMapper;

	public RegistrationServiceImpl(RegistrationDao registrationDao, ObjectMapper objectMapper) {
		this.registrationDao = registrationDao;
		this.objectMapper = objectMapper;
	}

	@Override
	public RegistrationResponse create(RegistrationRequest request) {
		String requestId = UUID.randomUUID().toString();
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
}
