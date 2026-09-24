package com.arif.hrs.util.excel.exceptions;

public class AnnotationNotFoundException extends Exception{
  
  public AnnotationNotFoundException(String fieldName){
    super(String.format("Excel Property Annotation for field %s is not found", fieldName));
  }
}
