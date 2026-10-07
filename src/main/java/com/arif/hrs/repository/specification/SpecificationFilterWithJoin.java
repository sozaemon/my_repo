package com.arif.hrs.repository.specification;

import java.util.List;

import com.arif.hrs.domain.dto.FilterDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificationFilterWithJoin extends SpecificationFilter {

  private String joinTable;
  private String joinField;
  private String joinType;

  public SpecificationFilterWithJoin(FilterDto dto) {
    this.setFieldName(dto.getFieldName());
    this.setValue(dto.getValue());
    this.setOperator(SpecificationEnum.valueOf(dto.getOperator()));
    this.setJoinFilter(dto.getJoinOperator());
    this.setValues(dto.getValues());
  }

  public SpecificationFilterWithJoin(
      String fieldName,
      Object value,
      SpecificationEnum operator,
      String joinFilter,
      List<Object> values) {
    this.setFieldName(fieldName);
    this.setValue(value);
    this.setOperator(operator);
    this.setJoinFilter(joinFilter);
    this.setValues(values);
  }

  public SpecificationFilterWithJoin(
      String fieldName,
      Object value,
      SpecificationEnum operator,
      String joinFilter) {
    super(fieldName, value, operator, joinFilter);
  }

  public SpecificationFilterWithJoin() {
  }

  public SpecificationFilterWithJoin(
      String fieldName,
      List<Object> values,
      SpecificationEnum operator,
      String joinFilter) {
    super(fieldName, values, operator, joinFilter);
  }

}
