package com.abhi1kush.registrationstore.valuebeans;

import java.time.Instant;

public record RegistrationEntity(
		String requestId,
		String registrationNo,
		String rollNo,
		String university,
		String college,
		String sourceSystem,
		String payloadJson,
		Instant createdAt) {
}
