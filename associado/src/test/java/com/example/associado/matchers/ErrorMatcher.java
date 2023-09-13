package com.example.associado.matchers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public abstract class ErrorMatcher {

  public static void toThrow(Throwable error, Executable executable) {
    if (error.getMessage() != null) {
      Assertions.assertThrows(error.getClass(), executable, error.getMessage());
    } else {
      Assertions.assertThrows(error.getClass(), executable);
    }
  }
}
