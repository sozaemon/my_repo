package com.arif.hrs.controllers.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseModel<String>> handleInternalException(Exception ex) {
    log.error("server error", ex);
    return ControllerUtil.createInternalServerErrorResponse(ex);
  }
}
