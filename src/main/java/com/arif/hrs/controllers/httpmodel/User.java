package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
  @JsonProperty("id")
  @NotNull(message = "id is required")
  private Integer id;
  @JsonProperty("userName")
  private String userName;
  @JsonProperty("email")
  @NotBlank(message = "email is required")
  @Email(message = "email must be a valid email address")
  private String email;
  @JsonProperty("expiryDate")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "expiryDate is required")
  private Timestamp expiryDate;
  @JsonProperty("fullName")
  @NotBlank(message = "fullName is required")
  private String fullName;
}
