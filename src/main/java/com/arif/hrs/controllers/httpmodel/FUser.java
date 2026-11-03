package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FUser {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("userName")
  @NotBlank(message = "userName is required")
  private String userName;
  @JsonProperty("email")
  @NotBlank(message = "email is required")
  @Email(message = "email must be a valid email address")
  private String email;
  @JsonProperty("expiryDate")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp expiryDate;
  @JsonProperty("fullName")
  @NotBlank(message = "fullName is required")
  private String fullName;
  @JsonProperty("plainPassword")
  @NotBlank(message = "plainPassword is required")
  @Size(min = 8, message = "plainPassword must be at least 8 characters")
  private String plainPassword;
}
