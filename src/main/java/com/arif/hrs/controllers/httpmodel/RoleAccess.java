package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RoleAccess {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("accessId")
  private Integer accessId;
  @JsonProperty("accessName")
  private String accessName;
  @JsonProperty("roleId")
  private Integer roleId;
  @JsonProperty("roleName")
  private String roleName;
}
