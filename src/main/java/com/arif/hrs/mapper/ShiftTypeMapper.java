package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.ShiftType;
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.model.ShiftTypeModel;

@Mapper(componentModel = "spring")
public interface ShiftTypeMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  public ShiftTypeDto modelToDto(ShiftTypeModel model);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  public ShiftTypeModel dtoToModel(ShiftTypeDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  public ShiftType dtoToHttp(ShiftTypeDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "code", target = "code")
  @Mapping(source = "name", target = "name")
  public ShiftTypeDto httpToDto(ShiftType http);
}
