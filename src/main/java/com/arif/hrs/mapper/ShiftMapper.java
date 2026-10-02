package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.Shift;
import com.arif.hrs.domain.dto.ShiftDto;
import com.arif.hrs.model.ShiftModel;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

  @Mapping(source = "shiftType.id", target = "shiftTypeId")
  @Mapping(source = "shiftType.name", target = "shiftTypeName")
  @Mapping(source = "inTolerance.id", target = "inToleranceId")
  @Mapping(source = "inTolerance.toleranceName", target = "inToleranceName")
  @Mapping(source = "outTolerance.id", target = "outToleranceId")
  @Mapping(source = "outTolerance.toleranceName", target = "outToleranceName")
  public ShiftDto modelToDto(ShiftModel model);

  @Mapping(source = "shiftTypeId", target = "shiftType.id")
  @Mapping(source = "inToleranceId", target = "inTolerance.id")
  @Mapping(source = "outToleranceId", target = "outTolerance.id")
  public ShiftModel dtoToModel(ShiftDto dto);

  public Shift dtoToHttp(ShiftDto dto);

  public ShiftDto httpToDto(Shift http);
}
