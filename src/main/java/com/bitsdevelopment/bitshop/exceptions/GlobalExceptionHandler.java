package com.bitsdevelopment.bitshop.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
    String message = "You do not have permission to this action";
    return new ResponseEntity<>(message, FORBIDDEN);
  }
}
