package com.abhi1kush.registrationstore.exception;

public class RegistrationException extends RuntimeException {

	private final String errorCode;

	public RegistrationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public RegistrationException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
