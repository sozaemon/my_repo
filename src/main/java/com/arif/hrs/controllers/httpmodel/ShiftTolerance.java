package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.arif.hrs.model.enums.ToleranceUnitEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ShiftTolerance {

  @JsonProperty("id")
  @NotNull(message = "id is required", groups = ValidationGroups.Update.class)
  private Integer id;

  @JsonProperty("code")
  @NotBlank(message = "code is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String code;

  @JsonProperty("toleranceName")
  @NotBlank(message = "toleranceName is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private String toleranceName;

  @JsonProperty("toleranceBeforeUnit")
  @NotNull(message = "toleranceBeforeUnit is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private ToleranceUnitEnum toleranceBeforeUnit;

  @JsonProperty("toleranceAfterUnit")
  @NotNull(message = "toleranceAfterUnit is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private ToleranceUnitEnum toleranceAfterUnit;

  @JsonProperty("toleranceBefore")
  @NotNull(message = "toleranceBefore is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  @PositiveOrZero(message = "toleranceBefore must be zero or greater", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private Integer toleranceBefore;

  @JsonProperty("toleranceAfter")
  @NotNull(message = "toleranceAfter is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  @PositiveOrZero(message = "toleranceAfter must be zero or greater", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private Integer toleranceAfter;
}
