package com.arif.hrs.domain.dto;

import com.arif.hrs.domain.dto.annotatons.SpecificationMapping;

import lombok.Data;

@Data
public class RoleAccessDto {

  private Integer id;
  @SpecificationMapping(joinTable = "access", targetColumn = "id")
  private Integer accessId;
  @SpecificationMapping(joinTable = "access", targetColumn = "name")
  private String accessName;
  @SpecificationMapping(joinTable = "role", targetColumn = "id")
  private Integer roleId;
  @SpecificationMapping(joinTable = "role", targetColumn = "name")
  private String roleName;

}
