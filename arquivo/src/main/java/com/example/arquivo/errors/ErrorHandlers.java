package com.example.arquivo.errors;

import com.example.common.errors.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandlers {
  private static final String MESSAGE = "message";
  private static final Map<String, String> internalError = Collections.singletonMap(
      MESSAGE,
      "Internal Server Error"
  );

  @Autowired
  private HttpServletRequest req;

  /**
   * Erro genérico para a aplicação
   * tratando todo erro não "conhecido" como 500
   */
  @ExceptionHandler({Exception.class, InternalServerError.class})
  public ResponseEntity<Object> handleException(Exception e) {
    log.error("Internal Server Error!", e);
    return ResponseEntity.internalServerError().body(internalError);
  }

  /**
   * Erros de validação por parte do client
   */
  @ExceptionHandler(BadRequestError.class)
  public ResponseEntity<Object> handleBadRequest(BadRequestError e) {
    Map<String, String> m = new HashMap<>();
    m.put(MESSAGE, e.getMessage());
    return ResponseEntity.badRequest().body(m);
  }

  /**
   * Erros ao consultar algo e não exsitir, not found
   */
  @ExceptionHandler(NotFoundError.class)
  public ResponseEntity<Object> handleNotFound(NotFoundError e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Collections.singletonMap(MESSAGE, e.getMessage()));
  }

  /**
   * Erros por falta de permissão, forbidden
   */
  @ExceptionHandler(ForbiddenError.class)
  public ResponseEntity<Object> handleForbidden(ForbiddenError e) {
    log.warn("Forbidden Error!", e);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(Collections.singletonMap(MESSAGE, e.getMessage()));
  }

  /**
   * Erro de cast do spring vindo por parte do client
   * <p>
   * No geral, um json mal formado tratado como BadRequest
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleValidations(HttpMessageNotReadableException e) {
    Map<String, Object> m = new HashMap<>();
    m.put("errors", new String[]{e.getMessage()});
    log.warn("erro de parser da aplicação!", e);
    return ResponseEntity.badRequest().body(m);
  }

  /**
   * Valores faltando na validação de algum objeto
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodParsingObjects(MethodArgumentNotValidException e) {
    List<FieldError> l = e.getFieldErrors();
    Map<String, Object> errors = new HashMap<>();

    for (FieldError f : l) {
      errors.put(f.getField(), f.getDefaultMessage());
    }

    Map<String, Object> body = new HashMap<>();
    body.put("errors", errors);
    return ResponseEntity.badRequest().body(body);
  }

  /**
   * Tratativa de métodos não suportados em uma rota X
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
    Map<String, Object> methods = new HashMap<>();
    methods.put("requested", e.getMethod());
    methods.put("allowed", e.getSupportedHttpMethods());

    Map<String, Object> response = new HashMap<>();
    response.put(MESSAGE, e.getMessage());
    response.put("methods", methods);
    response.put("path", req.getServletPath());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
  }

  /**
   * Tratativa de parametros faltando na request
   * quando o endpoint necessita deles
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingParameters(MissingServletRequestParameterException e) {
    Map<String, String> message = new HashMap<>();
    message.put(MESSAGE, "missing parameter in request url");
    message.put("parameter", e.getParameterName());
    message.put("parameterType", e.getParameterType());
    return ResponseEntity.badRequest().body(message);
  }

  /**
   * Tratativa para chamadas de serviços http
   */
  @ExceptionHandler(HttpRequestError.class)
  public ResponseEntity<Object> handleServiceRequestError(HttpRequestError e) {
    Map<String, Object> error = new HashMap<>();
    int status = e.getHttpStatus();

    error.put("info", "Service was not able to response correctly");
    error.put("serviceStatus", status);
    if (e.getMessage() != null && !e.getMessage().isEmpty()) {
      error.put(MESSAGE, e.getMessage());
    }

    return ResponseEntity.status(status).body(error);
  }

  /**
   * Erro na tentativa de chamar um serviço externo e ele estiver inatívo / com problema.
   */
  @ExceptionHandler(ServiceUnavailableError.class)
  public ResponseEntity<Object> handleServiceUnavailableError(ServiceUnavailableError e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, e.getMessage()));
  }
}
