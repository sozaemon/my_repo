package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.model.enums.AccessMethodEnum;
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
  private AccessMethodEnum method;
  @JsonProperty("description")
  private String description;
}
