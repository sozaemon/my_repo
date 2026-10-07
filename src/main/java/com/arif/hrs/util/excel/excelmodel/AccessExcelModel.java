package com.arif.hrs.util.excel.excelmodel;

import org.apache.poi.ss.usermodel.IndexedColors;

import com.arif.hrs.util.excel.annotation.ExcelProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessExcelModel {

  @ExcelProperty(header = "Name", index = 0)
  private String name;
  @ExcelProperty(header = "Path", index = 1)
  private String path;
  @ExcelProperty(header = "Method", index = 2)
  private String method;
  @ExcelProperty(header = "Description", index = 3, backgroundColor = IndexedColors.BRIGHT_GREEN)
  private String description;

}
