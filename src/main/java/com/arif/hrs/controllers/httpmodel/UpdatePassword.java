package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassword {
  @JsonProperty("oldPassword")
  String oldPassword;
  @JsonProperty("newPassword")
  String newPAssword;
}
