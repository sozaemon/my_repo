package com.arif.hrs.controllers.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.arif.hrs.controllers.ResponseModel;

@ControllerAdvice
public class AdviceController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ResponseModel<String>> handleResourceNotFound(NoResourceFoundException ne) {
    log.error("resource not found", ne);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseModel<>(ne));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseModel<String>> handleInternalException(Exception ex) {
    log.error("server error", ex);
    return ResponseEntity.internalServerError().body(new ResponseModel<>(ex));
  }
}
