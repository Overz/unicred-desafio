package com.example.common.errors;

public class NotFoundError extends RuntimeException {
	public NotFoundError() {
		this("Not Found!");
	}

	public NotFoundError(String message) {
		super(message);
	}
}
