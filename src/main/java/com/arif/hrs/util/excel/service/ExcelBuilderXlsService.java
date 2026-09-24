package com.arif.hrs.util.excel.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.arif.hrs.util.excel.ExcelBuilder;

public class ExcelBuilderXlsService<T> extends ExcelBuilder<T> {

  public ExcelBuilderXlsService(List<T> data, Class<T> clazz) {
    super(data, clazz);
  }

  @Override
  protected Workbook initiateWorkbook() {
    return new HSSFWorkbook();
  }

  @Override
  protected Sheet createWorkSheet() {
    return ((HSSFWorkbook) wb).createSheet();
  }
}
