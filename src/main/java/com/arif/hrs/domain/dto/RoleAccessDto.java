package com.arif.hrs.domain.dto;

import lombok.Data;

@Data
public class RoleAccessDto {

  private Integer id;
  private Integer accessId;
  private String accessName;
  private Integer roleId;
  private String roleName;

}
