package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FUser {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("userName")
  private String userName;
  @JsonProperty("email")
  private String email;
  @JsonProperty("expiryDate")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp expiryDate;
  @JsonProperty("fullName")
  private String fullName;
  @JsonProperty("plainPassword")
  private String plainPassword;
}
