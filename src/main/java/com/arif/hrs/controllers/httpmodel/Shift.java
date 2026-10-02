package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Shift {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("code")
  private String code;
  @JsonProperty("shiftName")
  private String shiftName;
  @JsonProperty("shiftTypeId")
  private Integer shiftTypeId;
  @JsonProperty("shiftTypeName")
  private String shiftTypeName;
  @JsonProperty("inTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp inTime;
  @JsonProperty("outTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
