package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;

import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Shift {
  @JsonProperty("id")
  @NotNull(message = "id is required", groups = ValidationGroups.Update.class)
  private Integer id;
  @JsonProperty("code")
  @NotBlank(message = "code is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private String code;
  @JsonProperty("shiftName")
  @NotBlank(message = "shiftName is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private String shiftName;
  @JsonProperty("shiftTypeId")
  @NotNull(message = "shiftTypeId is required", groups = { ValidationGroups.Create.class,
      ValidationGroups.Update.class })
  private Integer shiftTypeId;
  @JsonProperty("shiftTypeName")
  private String shiftTypeName;
  @JsonProperty("inTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "inTime is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private Timestamp inTime;
  @JsonProperty("outTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "outTime is required", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class })
  private Timestamp outTime;
  @JsonProperty("inToleranceId")
  private Integer inToleranceId;
  @JsonProperty("inToleranceName")
  private String inToleranceName;
  @JsonProperty("outToleranceId")
  private Integer outToleranceId;
  @JsonProperty("outToleranceName")
  private String outToleranceName;
}
