package com.arif.hrs.domain.dto;

import com.arif.hrs.model.enums.ToleranceUnitEnum;

import lombok.Data;

@Data
public class ShiftToleranceDto {

  private Integer id;

  private String code;

  private String toleranceName;

  private ToleranceUnitEnum toleranceBeforeUnit;

  private ToleranceUnitEnum toleranceAfterUnit;

  private Integer toleranceBefore;

  private Integer toleranceAfter;

}
