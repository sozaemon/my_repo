package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleAccessRequest {

  @JsonProperty("accessId")
  @NotNull(message = "accessId is required")
  @Positive(message = "accessId must be positive")
  private Integer accessId;
  @JsonProperty("roleIds")
  @NotEmpty(message = "roleIds is required")
  private List<@NotNull(message = "roleIds must not contain null") @Positive(message = "roleIds must be positive") Integer> roleIds;
}
