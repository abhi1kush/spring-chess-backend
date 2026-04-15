package com.abhi1kush.registrationstore.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhi1kush.registrationstore.exception.RegistrationException;
import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {
	private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

	private final RegistrationService registrationService;

	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody RegistrationRequest request, BindingResult bindingResult) {
		log.info("Received registration create request registrationNo={} sourceSystem={}",
				request.getRegistrationNo(), request.getSourceSystem());
		if (bindingResult.hasErrors()) {
			Map<String, String> fieldErrors = new LinkedHashMap<>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				fieldErrors.putIfAbsent(fieldError.getField(),
						fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage());
			}
			Map<String, Object> body = new LinkedHashMap<>();
			body.put("status", "0");
			body.put("errorCode", "VALIDATION_ERROR");
			body.put("errorDesc", "Validation failed");
			body.put("fieldErrors", fieldErrors);
			log.warn("Validation failed for registration request fields={}", fieldErrors.keySet());
			return ResponseEntity.badRequest().body(body);
		}
		try {
			RegistrationResponse response = registrationService.create(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (DuplicateKeyException e) {
			log.warn("Duplicate registration request registrationNo={}", request.getRegistrationNo(), e);
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new RegistrationResponse("0", "DUPLICATE_REGISTRATION", "registrationNo already exists", ""));
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
