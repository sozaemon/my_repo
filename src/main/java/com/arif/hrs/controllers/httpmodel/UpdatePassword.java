package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassword {
  @JsonProperty("oldPassword")
  @NotBlank(message = "oldPassword is required")
  String oldPassword;
  @JsonProperty("newPassword")
  @NotBlank(message = "newPassword is required")
  @Size(min = 8, message = "newPassword must be at least 8 characters")
  String newPAssword;
}
