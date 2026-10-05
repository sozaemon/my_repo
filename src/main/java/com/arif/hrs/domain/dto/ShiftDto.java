package com.arif.hrs.domain.dto;

import java.sql.Timestamp;

import com.arif.hrs.domain.dto.annotatons.SpecificationMapping;

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
  @SpecificationMapping(joinTable = "shiftType", targetColumn = "id")
  private Integer shiftTypeId;
  @SpecificationMapping(joinTable = "shiftType", targetColumn = "name")
  private String shiftTypeName;
  private Timestamp inTime;
  private Timestamp outTime;
  @SpecificationMapping(joinTable = "inTolerance", targetColumn = "id")
  private Integer inToleranceId;
  @SpecificationMapping(joinTable = "inTolerance", targetColumn = "toleranceName")
  private String inToleranceName;
  @SpecificationMapping(joinTable = "outTolerance", targetColumn = "id")
  private Integer outToleranceId;
  @SpecificationMapping(joinTable = "outTolerance", targetColumn = "toleranceName")
  private String outToleranceName;
}
