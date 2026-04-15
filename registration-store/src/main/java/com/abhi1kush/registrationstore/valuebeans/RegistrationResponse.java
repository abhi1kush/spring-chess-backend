package com.abhi1kush.registrationstore.valuebeans;

public record RegistrationResponse(
		String status,
		String errorCode,
		String errorDesc,
		String refNo) {
}
