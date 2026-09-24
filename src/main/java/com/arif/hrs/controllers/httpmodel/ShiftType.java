package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShiftType {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("code")
  private String code;
  @JsonProperty("name")
  private String name;

}
