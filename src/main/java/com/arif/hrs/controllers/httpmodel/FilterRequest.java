package com.arif.hrs.controllers.httpmodel;

import java.sql.Timestamp;
import java.util.List;

import com.arif.hrs.repository.specification.SpecificationFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterRequest {
  @JsonProperty("fieldName")
  private String fieldName;

  @JsonProperty("value")
  private Object value;

  @JsonProperty("timeValue")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Timestamp timeValue;
  
  @JsonProperty("values")
  private List<Object> values;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  @JsonProperty("timeValues")
  private List<Timestamp> timeValues;

  @JsonProperty("operator")
  private String operator;
  
  @JsonProperty("joinOperator")
  private String joinOperator;

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getOperator() {
    if (operator == null) {
      return "EQ";
    }
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getJoinOperator() {
    if (joinOperator == null) {
      return SpecificationFilter.JOIN_FILTER_AND;
    }
    return joinOperator;
  }

  public void setJoinOperator(String joinOperator) {
    this.joinOperator = joinOperator;
  }

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  public Timestamp getTimeValue() {
    return timeValue;
  }

  public void setTimeValue(Timestamp timeValue) {
    this.timeValue = timeValue;
  }

  public List<Timestamp> getTimeValues() {
    return timeValues;
  }

  public void setTimeValues(List<Timestamp> timeValues) {
    this.timeValues = timeValues;
  }
}
