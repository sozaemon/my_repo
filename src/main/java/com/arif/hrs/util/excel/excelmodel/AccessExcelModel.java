package com.arif.hrs.util.excel.excelmodel;

import org.apache.poi.ss.usermodel.IndexedColors;

import com.arif.hrs.util.excel.annotation.ExcelProperty;

public class AccessExcelModel {

  @ExcelProperty(header = "Name", index = 0)
  private String name;
  @ExcelProperty(header = "Path", index = 1)
  private String path;
  @ExcelProperty(header = "Method", index = 2)
  private String method;
  @ExcelProperty(header = "Description", index = 3, backgroundColor = IndexedColors.BRIGHT_GREEN)
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
