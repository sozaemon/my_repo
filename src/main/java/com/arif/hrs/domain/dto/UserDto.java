package com.arif.hrs.domain.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UserDto {
  private Integer id;
  private String userName;
  private String email;
  private Timestamp expiryDate;
  private String fullName;

}
