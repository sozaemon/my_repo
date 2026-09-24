package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.RoleAccess;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.model.RoleAccessModel;

@Mapper(componentModel = "spring")
public interface RoleAccessMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "access.id", target = "accessId")
  @Mapping(source = "access.name", target = "accessName")
  @Mapping(source = "role.id", target = "roleId")
  @Mapping(source = "role.name", target = "roleName")
  public RoleAccessDto modelToDto(RoleAccessModel model);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "accessId", target = "access.id")
  @Mapping(source = "roleId", target = "role.id")
  public RoleAccessModel dtoToModel(RoleAccessDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "accessId", target = "accessId")
  @Mapping(source = "accessName", target = "accessName")
  @Mapping(source = "roleId", target = "roleId")
  @Mapping(source = "roleName", target = "roleName")
  public RoleAccess dtoToHttp(RoleAccessDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "accessId", target = "accessId")
  @Mapping(source = "accessName", target = "accessName")
  @Mapping(source = "roleId", target = "roleId")
  @Mapping(source = "roleName", target = "roleName")
  public RoleAccessDto httpToDto(RoleAccess http);

}
