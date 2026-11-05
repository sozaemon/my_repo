package com.arif.hrs.domain.dto;

import lombok.Data;

@Data 
public class PermissionDto {
  private Integer id;
  private String roleCode;
  private String permission;
}
