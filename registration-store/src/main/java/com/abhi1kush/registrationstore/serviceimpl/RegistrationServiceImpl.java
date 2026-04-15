package com.abhi1kush.registrationstore.serviceimpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.abhi1kush.registrationstore.dao.RegistrationDao;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationEntity;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	private final RegistrationDao registrationDao;
	private final ObjectMapper objectMapper;

	public RegistrationServiceImpl(RegistrationDao registrationDao, ObjectMapper objectMapper) {
		this.registrationDao = registrationDao;
		this.objectMapper = objectMapper;
	}

	@Override
	public RegistrationResponse create(RegistrationRequest request) {
		String requestId = UUID.randomUUID().toString();
		String payloadJson;
		try {
			payloadJson = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Unable to serialize request", e);
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
		registrationDao.insert(entity);
		return new RegistrationResponse("1", "", "", requestId);
	}
}
