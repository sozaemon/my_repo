package com.arif.hrs.util.excel;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelGenerator {
  private ExcelGenerator() {
  }

  public static <T> Workbook createExcel(ExcelBuilder<T> builder) {
    return builder.buildWorkbook();
  }
}
