package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

  @JsonProperty("userName")
  @NotBlank(message = "userName is required")
  private String userName;
  @JsonProperty("password")
  @NotBlank(message = "password is required")
  private String password;
  @JsonProperty("role")
  @NotBlank(message = "role is required")
  private String role;

}
