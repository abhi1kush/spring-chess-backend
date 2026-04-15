package com.abhi1kush.registrationstore.service;

import com.abhi1kush.registrationstore.valuebeans.RegistrationRequest;
import com.abhi1kush.registrationstore.valuebeans.RegistrationResponse;

public interface RegistrationService {
	RegistrationResponse create(RegistrationRequest request);
}
