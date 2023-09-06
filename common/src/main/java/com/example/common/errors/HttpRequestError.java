package com.example.common.errors;

public class HttpRequestError extends RuntimeException {
	private final int httpStatus;

	public HttpRequestError(int httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}
}
