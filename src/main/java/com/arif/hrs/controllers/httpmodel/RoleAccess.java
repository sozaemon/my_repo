package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoleAccess {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("accessId")
  @NotNull(message = "accessId is required")
  @Positive(message = "accessId must be positive")
  private Integer accessId;
  @JsonProperty("accessName")
  private String accessName;
  @JsonProperty("roleId")
  @NotNull(message = "roleId is required")
  @Positive(message = "roleId must be positive")
  private Integer roleId;
  @JsonProperty("roleName")
  private String roleName;
}
