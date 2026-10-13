package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserWithPassword extends User {
  @JsonProperty("password")
  private String password;
}
