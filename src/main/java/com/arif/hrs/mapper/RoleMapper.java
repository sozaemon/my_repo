package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.Role;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.model.RoleModel;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "active", target = "active")
  RoleDto modelToDto(RoleModel model);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "active", target = "active")
  RoleModel dtoToModel(RoleDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "active", target = "active")
  Role dtoToHttp(RoleDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "active", target = "active")
  RoleDto httpToDto(Role http);

}
