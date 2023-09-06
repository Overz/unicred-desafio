package com.example.common.errors;

public class ForbiddenError extends RuntimeException {
	public ForbiddenError() {
		this("Forbidden!");
	}

	public ForbiddenError(String message) {
		super(message);
	}

	public ForbiddenError(String message, Throwable cause) {
		super(message, cause);
	}
}
