package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AssignUserRole {

  @JsonProperty("userId")
  @NotNull(message = "userId is required")
  @Positive(message = "userId must be positive")
  private Integer userId;
  @JsonProperty("roleId")
  @NotEmpty(message = "roleId is required")
  private List<@NotNull(message = "roleId must not contain null") @Positive(message = "roleId must be positive") Integer> roleId;
}
