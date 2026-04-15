package com.abhi1kush.registrationstore.exception;

public class RegistrationPersistenceException extends RegistrationException {

	public RegistrationPersistenceException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
}
