package com.arif.hrs.mapper;

import org.mapstruct.Mapper;

import com.arif.hrs.controllers.httpmodel.Permission;
import com.arif.hrs.domain.dto.PermissionDto;
import com.arif.hrs.model.PermissionModel;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

  Permission dtoToHttp(PermissionDto dto);

  PermissionDto httpToDto(Permission http);

  PermissionModel dtoToModel(PermissionDto dto);

  PermissionDto modelToDto(PermissionModel model);
}
