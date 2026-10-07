package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleAccessRequest {

  @JsonProperty("accessId")
  private Integer accessId;
  @JsonProperty("roleIds")
  private List<Integer> roleIds;
}
