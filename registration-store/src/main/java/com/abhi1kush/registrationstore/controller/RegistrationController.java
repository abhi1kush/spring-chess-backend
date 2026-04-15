package com.abhi1kush.registrationstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abhi1kush.registrationstore.service.RegistrationService;
import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {

	private final RegistrationService registrationService;

	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RegistrationResponse create(@Valid @RequestBody RegistrationRequest request) {
		return registrationService.create(request);
	}
}
