package com.example.common.errors;

public class MapperError extends RuntimeException {
  private final Class<?> target;

  public MapperError(String message, Class<?> target, Throwable e) {
    super(message, e);
    this.target = target;
  }

  public Class<?> getTarget() {
    return target;
  }
}
