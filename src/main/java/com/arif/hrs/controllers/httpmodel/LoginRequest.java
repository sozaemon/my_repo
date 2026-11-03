package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.arif.hrs.domain.dto.annotations.ValidationGroups;
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
  @NotBlank(message = "role is required", groups= ValidationGroups.LoginWithRole.class)
  private String role;

}
