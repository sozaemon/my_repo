package com.arif.hrs.controllers;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T> {

  @JsonProperty("responseTime")
  private Timestamp responseTime;
  @JsonProperty("data")
  private T data;
  private @JsonProperty("message") String message;
  private @JsonProperty("error") String error;

  public ResponseModel(T data, String message) {
    this.responseTime = Timestamp.from(Instant.now());
    this.data = data;
    this.message = message;
  }

  public ResponseModel(Exception e) {
    this.responseTime = Timestamp.from(Instant.now());

    if (e != null) {
      this.error = e.getLocalizedMessage();
    }

    if (e instanceof NoResourceFoundException) {
      this.message = "resource not found";
    }

    if (e instanceof AuthenticationException) {
      this.message = "unauthorized";
    }

    if (e instanceof AccessDeniedException) {
      this.message = "cannot access resource";
    }

  }
}
