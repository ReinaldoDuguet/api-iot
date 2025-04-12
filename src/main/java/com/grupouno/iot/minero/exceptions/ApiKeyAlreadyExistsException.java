package com.grupouno.iot.minero.exceptions;

public class ApiKeyAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ApiKeyAlreadyExistsException(String message) {
		super(message);
	}

}
