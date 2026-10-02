package com.arif.hrs.domain.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShiftDto {
  private Integer id;
  private String code;
  private String shiftName;
  private Integer shiftTypeId;
  private String shiftTypeName;
  private Timestamp inTime;
  private Timestamp outTime;
  private Integer inToleranceId;
  private String inToleranceName;
  private Integer outToleranceId;
  private String outToleranceName;
}
