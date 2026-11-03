package com.arif.hrs.controllers.advice;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.util.ControllerUtil;

@ControllerAdvice
public class AdviceController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ResponseModel<String>> handleResourceNotFound(NoResourceFoundException ne) {
    log.error("resource not found", ne);
    return ControllerUtil.createResourceNotFoundResponse(ne);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseModel<String>> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    log.warn("validation failed: {}", message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ResponseModel<String>(message, "validation failed"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseModel<String>> handleInternalException(Exception ex) {
    log.error("server error", ex);
    return ControllerUtil.createInternalServerErrorResponse(ex);
  }
}
