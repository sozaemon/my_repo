package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.AssignUserRole;
import com.arif.hrs.domain.dto.AssignUserRoleDto;

@Mapper(componentModel = "spring")
public interface AssignUserRoleMapper {

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "roleId", target = "roleId")
  AssignUserRoleDto httpToDto(AssignUserRole http);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "roleId", target = "roleId")
  AssignUserRole dtoToHttp(AssignUserRoleDto dto);
}
