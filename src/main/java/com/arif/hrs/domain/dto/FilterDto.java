package com.arif.hrs.domain.dto;

import java.util.List;

import com.arif.hrs.controllers.httpmodel.FilterRequest;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilter;

public class FilterDto {

  private String fieldName;
  private Object value;
  private List<Object> values;
  private String operator;
  private String joinOperator;

  public FilterDto() {
  }

  public FilterDto(FilterRequest request) {
    if (request.getTimeValue() != null) {
      this.value = request.getTimeValue();
    } else {
      this.value = request.getValue();
    }
    this.fieldName = request.getFieldName();
    this.values = request.getValues();
    this.operator = request.getOperator();
    this.joinOperator = request.getJoinOperator();
  }

  public SpecificationFilter toSpecification() {
    if (getValue() != null) {
      return new SpecificationFilter(
          getFieldName(),
          getValue(),
          SpecificationEnum.valueOf(getOperator()),
          getJoinOperator());
    }
    if (getValues() != null) {
      return new SpecificationFilter(
          getFieldName(),
          getValues(),
          SpecificationEnum.valueOf(getOperator()),
          getJoinOperator());
    }
    return null;
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

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getJoinOperator() {
    return joinOperator;
  }

  public void setJoinOperator(String joinOperator) {
    this.joinOperator = joinOperator;
  }

}
