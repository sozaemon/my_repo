package com.arif.hrs.util.excel.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.arif.hrs.util.excel.ExcelBuilder;

public class ExcelBuilderXlsxService<T> extends ExcelBuilder<T> {

  public ExcelBuilderXlsxService(List<T> data, Class<T> clazz) {
    super(data, clazz);
  }

  @Override
  protected Workbook initiateWorkbook() {
    return new XSSFWorkbook();
  }

  @Override
  protected Sheet createWorkSheet() {
    return ((XSSFWorkbook) wb).createSheet();
  }
}
