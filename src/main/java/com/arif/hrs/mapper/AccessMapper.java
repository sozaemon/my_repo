package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.Access;
import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.util.excel.excelmodel.AccessExcelModel;

@Mapper(componentModel = "spring")
public interface AccessMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "path", target = "path")
  @Mapping(source = "method", target = "method")
  @Mapping(source = "description", target = "description")
  Access dtoToHttp(AccessDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "path", target = "path")
  @Mapping(source = "method", target = "method")
  @Mapping(source = "description", target = "description")
  AccessModel dtoToModel(AccessDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "path", target = "path")
  @Mapping(source = "method", target = "method")
  @Mapping(source = "description", target = "description")
  AccessDto modelToDto(AccessModel model);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "path", target = "path")
  @Mapping(source = "method", target = "method")
  @Mapping(source = "description", target = "description")
  AccessDto httpToDto(Access http);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "path", target = "path")
  @Mapping(source = "method", target = "method")
  @Mapping(source = "description", target = "description")
  AccessExcelModel modelToExcel(AccessModel dto);

}
