package com.abhi1kush.registrationstore.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

@RestControllerAdvice
public class RegistrationExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(RegistrationExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
		Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(
						error -> error.getField(),
						error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(),
						(first, second) -> first,
						LinkedHashMap::new));
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", "0");
		body.put("errorCode", "VALIDATION_ERROR");
		body.put("errorDesc", "Validation failed");
		body.put("fieldErrors", fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<RegistrationResponse> handleDuplicate(DuplicateKeyException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new RegistrationResponse("0", "DUPLICATE_REGISTRATION", "registrationNo already exists", ""));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RegistrationResponse> handleAny(Exception e) {
		log.error("Unhandled registration API exception", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new RegistrationResponse("0", "INTERNAL_ERROR", "Unexpected error", ""));
	}
}
