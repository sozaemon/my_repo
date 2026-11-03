package com.arif.hrs.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssignUserRoleDto {

  private Integer userId;
  private List<Integer> roleId;
}
