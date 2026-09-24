package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Access {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("path")
  private String path;
  @JsonProperty("method")
  private String method;
  @JsonProperty("description")
  private String description;
}
