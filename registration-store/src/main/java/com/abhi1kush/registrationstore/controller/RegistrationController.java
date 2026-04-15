package com.abhi1kush.registrationstore.controller;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhi1kush.registrationstore.exception.RegistrationException;
import com.abhi1kush.registrationstore.exception.RegistrationValidationException;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {
	private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

	private final RegistrationService registrationService;

	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody RegistrationRequest request) {
		log.info("Received registration create request registrationNo={} sourceSystem={}",
				request.getRegistrationNo(), request.getSourceSystem());
		try {
			RegistrationResponse response = registrationService.create(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (DuplicateKeyException e) {
			log.warn("Duplicate registration request registrationNo={}", request.getRegistrationNo(), e);
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new RegistrationResponse("0", "DUPLICATE_REGISTRATION", "registrationNo already exists", ""));
		} catch (RegistrationValidationException e) {
			log.warn("Registration validation failed registrationNo={} errorCode={}",
					request.getRegistrationNo(), e.getErrorCode());
			Map<String, Object> body = new java.util.LinkedHashMap<>();
			body.put("status", "0");
			body.put("errorCode", e.getErrorCode());
			body.put("errorDesc", e.getMessage());
			if (!e.getFieldErrors().isEmpty()) {
				body.put("fieldErrors", e.getFieldErrors());
			}
			return ResponseEntity.badRequest().body(body);
		} catch (RegistrationException e) {
			log.error("Registration exception errorCode={} registrationNo={}",
					e.getErrorCode(), request.getRegistrationNo(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RegistrationResponse("0", e.getErrorCode(), e.getMessage(), ""));
		} catch (Exception e) {
			log.error("Unhandled registration API exception registrationNo={}", request.getRegistrationNo(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RegistrationResponse("0", "INTERNAL_ERROR", "Unexpected error", ""));
		}
	}
}
