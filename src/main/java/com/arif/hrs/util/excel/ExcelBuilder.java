package com.arif.hrs.util.excel;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arif.hrs.util.excel.annotation.ExcelProperty;
import com.arif.hrs.util.excel.exceptions.AnnotationNotFoundException;

public abstract class ExcelBuilder<T> {
  protected final Logger log = LoggerFactory.getLogger(getClass());

  protected final Class<?> clazz;
  protected final List<T> dataSet;

  protected Workbook wb;
  protected Sheet sheet;

  protected int headerRow = 1;
  protected int startDataRow = 2;

  protected ExcelBuilder(List<T> dataSet, Class<?> clazz) {
    this.clazz = clazz;
    this.dataSet = dataSet;
    log.info("initiate class type {}", this.clazz);
  }

  public Workbook buildWorkbook() {
    log.info("initiate xlsx workbook build");
    this.wb = initiateWorkbook();

    try {

      Map<String, ExcelProperty> property = extractExcelProperty();

      this.sheet = createWorkSheet();
      createHeaderRow(property);
      createDataRows(property);

      Set<?> entrySet = property.entrySet();

      int c = 0;
      for (@SuppressWarnings("unused")
      Object o : entrySet) {
        sheet.autoSizeColumn(c);
        c++;
      }

    } catch (Exception e) {
      log.error("fail to populate excel data", e);
    }

    return wb;
  }

  protected abstract Workbook initiateWorkbook();

  protected abstract Sheet createWorkSheet();

  protected CellStyle createCellStyle(IndexedColors backgroundColor) {
    CellStyle style = wb.createCellStyle();
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFillForegroundColor(backgroundColor.getIndex());

    return style;
  }

  protected void createDataRows(Map<String, ExcelProperty> property) {
    int rowCounter = this.startDataRow;

    Map<IndexedColors, CellStyle> styles = new EnumMap<>(IndexedColors.class);

    for (T d : dataSet) {
      Row dataRow = sheet.createRow(rowCounter);
      property.forEach((k, v) -> {
        Cell cell = dataRow.createCell(v.index());
        try {
          PropertyDescriptor pd = new PropertyDescriptor(k, d.getClass());
          Method md = pd.getReadMethod();
          Object fieldValue = md.invoke(d);

          fillDataCellValue(cell, fieldValue, v);

          CellStyle st = styles.get(v.backgroundColor());
          if (st != null) {
            cell.setCellStyle(st);
          } else {
            st = createCellStyle(v.backgroundColor());
            styles.put(v.backgroundColor(), st);
            cell.setCellStyle(st);
          }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException io) {
          log.error("fail to create cell data", io);
          return;
        }
      });
      rowCounter++;
    }
  }

  private void fillDataCellValue(Cell cell, Object fieldValue, ExcelProperty prop) {
    if (Objects.isNull(fieldValue)) {
      return;
    }

    switch (prop.dataType()) {
      case ExcelProperty.DataType.STRING_TYPE:
        cell.setCellValue((String) fieldValue);
        break;
      case ExcelProperty.DataType.NUMBER_TYPE:
        cell.setCellValue((Long) fieldValue);
        break;
      case ExcelProperty.DataType.FLOAT_TYPE:
        BigDecimal bd = BigDecimal.valueOf((Double) fieldValue);
        bd = bd.setScale(prop.floatPlace(), RoundingMode.HALF_UP);
        cell.setCellValue(bd.doubleValue());
        break;
      case ExcelProperty.DataType.TIME_TYPE:
        Timestamp t = (Timestamp) fieldValue;
        SimpleDateFormat df = new SimpleDateFormat(prop.timeFormat());
        cell.setCellValue(df.format(t));
        break;
    }
  }

  protected void createHeaderRow(Map<String, ExcelProperty> property) {

    CellStyle style = wb.createCellStyle();
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFillForegroundColor(IndexedColors.WHITE.getIndex());

    List<ExcelProperty> prop = new ArrayList<>();

    property.forEach((k, v) -> prop.add(v));

    String[] headers = prop.stream()
        .sorted((a, b) -> Integer.compare(a.index(), b.index()))
        .map(m -> m.header()).toArray(String[]::new);

    Row row = sheet.createRow(headerRow);

    int cellCounter = 0;
    for (String h : headers) {
      Cell cell = row.createCell(cellCounter);
      cell.setCellValue(h);
      cell.setCellStyle(style);
      cellCounter++;
    }
  }

  private Map<String, ExcelProperty> extractExcelProperty()
      throws AnnotationNotFoundException, SecurityException {

    log.info("extract excel property");
    Map<String, ExcelProperty> result = new HashMap<>();

    Field[] fields = clazz.getDeclaredFields();

    for (Field f : fields) {
      if (f.isAnnotationPresent(ExcelProperty.class)) {
        result.put(f.getName(), f.getAnnotation(ExcelProperty.class));
      } else {
        throw new AnnotationNotFoundException(f.getName());
      }
    }
    return result;
  }
}
