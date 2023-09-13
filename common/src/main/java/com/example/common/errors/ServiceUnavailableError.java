package com.example.common.errors;

public class ServiceUnavailableError extends RuntimeException {
  public ServiceUnavailableError() {
    super("Serviço indisponível, tente novamente mais tarde!");
  }

  public ServiceUnavailableError(String message) {
    super(message);
  }

  public ServiceUnavailableError(String message, Throwable cause) {
    super(message, cause);
  }
}
