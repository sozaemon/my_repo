package com.arif.hrs.util.excel.excelmodel;

import com.arif.hrs.util.excel.annotation.ExcelProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleExcelModel {

  @ExcelProperty(header = "Code", index = 0)
  private String code;

  @ExcelProperty(header = "Name", index = 1)
  private String name;

  @ExcelProperty(header = "Description", index = 2)
  private String description;

}
