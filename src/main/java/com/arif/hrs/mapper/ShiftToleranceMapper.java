package com.arif.hrs.mapper;

import org.mapstruct.Mapper;

import com.arif.hrs.controllers.httpmodel.ShiftTolerance;
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.model.ShiftToleranceModel;

@Mapper(componentModel = "spring")
public interface ShiftToleranceMapper {

  public ShiftToleranceModel dtoToModel(ShiftToleranceDto dto);

  public ShiftToleranceDto modelToDto(ShiftToleranceModel model);

  public ShiftTolerance dtoToHttp(ShiftToleranceDto dto);

  public ShiftToleranceDto httpToDto(ShiftTolerance http);
}
