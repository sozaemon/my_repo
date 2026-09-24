package com.arif.hrs.domain.dto;

import lombok.Data;

@Data
public class RoleDto {

  private Integer id;
  private String code;
  private String name;
  private String description;
  private Boolean active;
}
