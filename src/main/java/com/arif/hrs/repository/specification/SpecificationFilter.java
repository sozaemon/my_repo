package com.arif.hrs.repository.specification;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificationFilter {
  private String fieldName;
  private Object value;
  private List<Object> values;
  private SpecificationEnum operator;
  private String joinFilter;

  private String joinTable;
  private String joinField;
  private String joinType;

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
}
