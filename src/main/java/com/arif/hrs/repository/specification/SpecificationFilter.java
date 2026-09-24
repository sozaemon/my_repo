package com.arif.hrs.repository.specification;

import java.util.List;

public class SpecificationFilter {
  private String fieldName;
  private Object value;
  private List<Object> values;
  private SpecificationEnum operator;
  private String joinFilter;

  public static final String JOIN_FILTER_OR = "OR";
  public static final String JOIN_FILTER_AND = "AND";

  public SpecificationFilter() {
  }

  public SpecificationFilter(String fieldName, Object value, SpecificationEnum operator, String joinFilter) {
    this.fieldName = fieldName;
    this.value = value;
    this.operator = operator;
    this.joinFilter = joinFilter;
  }

  public SpecificationFilter(String fieldName, List<Object> values, SpecificationEnum operator, String joinFilter) {
    this.fieldName = fieldName;
    this.values = values;
    this.operator = operator;
    this.joinFilter = joinFilter;
  }

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

  public SpecificationEnum getOperator() {
    return operator;
  }

  public void setOperator(SpecificationEnum operator) {
    this.operator = operator;
  }

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  public String getJoinFilter() {
    return joinFilter;
  }

  public void setJoinFilter(String joinFilter) {
    this.joinFilter = joinFilter;
  }

}
