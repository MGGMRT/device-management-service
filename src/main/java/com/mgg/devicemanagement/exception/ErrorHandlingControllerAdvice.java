package com.mgg.devicemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> onConstraintValidationException(
      ConstraintViolationException e) {
    List<Violation> violations =
        e.getConstraintViolations().stream()
            .map(
                violation ->
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
            .collect(Collectors.toList());
    return ResponseEntity.badRequest().body(new ValidationErrorResponse(violations));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ValidationErrorResponse> handleException(
      MethodArgumentNotValidException e) {
    List<Violation> violations =
        e.getBindingResult().getAllErrors().stream()
            .filter(FieldError.class::isInstance)
            .map(FieldError.class::cast)
            .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.toList());
    return ResponseEntity.badRequest().body(new ValidationErrorResponse(violations));
  }

  @ExceptionHandler(NotFoundDeviceException.class)
  public ResponseEntity<Object> onNotFoundDeviceException(NotFoundDeviceException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(UnRecognizedDevicePatchException.class)
  public ResponseEntity<Object> onUnRecognizedDevicePatchException(UnRecognizedDevicePatchException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<Object> onOptimisticLockException(OptimisticLockException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
  public ResponseEntity<Object> onInternalServerError(
      HttpServerErrorException.InternalServerError e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> onHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> onException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected Error!, please call customer center");
  }
}
