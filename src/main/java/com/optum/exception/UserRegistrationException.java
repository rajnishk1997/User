package com.optum.exception;

public class UserRegistrationException extends RuntimeException {
	
	 private static final long serialVersionUID = 1L;

    public UserRegistrationException() {
        super();
    }

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

