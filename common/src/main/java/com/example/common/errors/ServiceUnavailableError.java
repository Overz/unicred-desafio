package com.example.common.errors;

public class ServiceUnavailableError extends RuntimeException {
	public ServiceUnavailableError(String message) {
		super(message);
	}

	public ServiceUnavailableError() {
		super("Serviço indisponível, tente novamente mais tarde!");
	}
}
