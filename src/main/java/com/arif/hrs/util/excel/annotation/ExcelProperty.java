package com.arif.hrs.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.IndexedColors;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelProperty {

  String header() default "";

  int index() default 1;

  enum DataType {
    STRING_TYPE,
    FLOAT_TYPE,
    NUMBER_TYPE,
    TIME_TYPE
  }

  DataType dataType() default DataType.STRING_TYPE;

  IndexedColors backgroundColor() default IndexedColors.WHITE;

  String timeFormat() default "YYYY/MM/dd";

  int floatPlace() default 2;
}
