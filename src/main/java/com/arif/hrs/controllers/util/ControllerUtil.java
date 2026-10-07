package com.arif.hrs.controllers.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.arif.hrs.controllers.ResponseModel;

public class ControllerUtil {

  private ControllerUtil() {
  }

  public static <T> ResponseEntity<ResponseModel<T>> createSuccessResponse(T input, String message) {
    return ResponseEntity.ok(new ResponseModel<T>(input, message));
  }

  public static ResponseEntity<ResponseModel<String>> createResourceNotFoundResponse(NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseModel<>(e));
  }

  public static ResponseEntity<ResponseModel<String>> createInternalServerErrorResponse(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel<>(e));
  }
}
