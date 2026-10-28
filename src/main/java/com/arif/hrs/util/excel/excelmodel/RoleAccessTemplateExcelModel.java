package com.arif.hrs.util.excel.excelmodel;

import com.arif.hrs.util.excel.annotation.ExcelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Column layout of the role access upload template. The headers must match the
 * keys read by the role access upload process.
 */
@Getter
@Setter
public class RoleAccessTemplateExcelModel {

  @ExcelProperty(header = "Name", index = 0)
  private String name;

  @ExcelProperty(header = "Path", index = 1)
  private String path;

  @ExcelProperty(header = "Method", index = 2)
  private String method;

  @ExcelProperty(header = "Description", index = 3)
  private String description;

  @ExcelProperty(header = "Roles", index = 4)
  private String roles;

}
