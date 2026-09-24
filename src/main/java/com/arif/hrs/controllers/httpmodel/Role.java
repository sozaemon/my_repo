package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Role {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("code")
  private String code;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("active")
  private Boolean active;

}
