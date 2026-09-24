package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.model.enums.ToleranceUnitEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShiftTolerance {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("code")
  private String code;

  @JsonProperty("toleranceName")
  private String toleranceName;

  @JsonProperty("toleranceBeforeUnit")
  private ToleranceUnitEnum toleranceBeforeUnit;

  @JsonProperty("toleranceAfterUnit")
  private ToleranceUnitEnum toleranceAfterUnit;

  @JsonProperty("toleranceBefore")
  private Integer toleranceBefore;

  @JsonProperty("toleranceAfter")
  private Integer toleranceAfter;
}
