package com.example.common.errors;

public class InternalServerError extends RuntimeException {
	public InternalServerError() {
		this("Internal Server Error!");
	}

	public InternalServerError(String message) {
		super(message);
	}

	public InternalServerError(String message, Throwable cause) {
		super(message, cause);
	}
}
