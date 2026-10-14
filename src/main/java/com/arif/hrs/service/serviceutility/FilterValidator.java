package com.arif.hrs.service.serviceutility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.arif.hrs.domain.dto.annotatons.SpecificationMapping;
import com.arif.hrs.repository.specification.SpecificationFilterWithJoin;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterValidator {
  private List<SpecificationFilterWithJoin> basicFilters = new ArrayList<>();
  private Class<?> clazz;

  public FilterValidator(Class<?> clazz) {
    this.clazz = clazz;
  }

  public FilterValidator(List<SpecificationFilterWithJoin> filters, Class<?> clazz) {
    this.basicFilters = filters;
    this.clazz = clazz;
  }

  public List<SpecificationFilterWithJoin> buildAndValidateFilters() {

    List<FieldJoinMapping> mappingProperty = this.extractMappingProperty();

    for (SpecificationFilterWithJoin f : basicFilters) {

      Optional<FieldJoinMapping> mapping = mappingProperty.stream().filter(p -> p.fieldName().equals(f.getFieldName()))
          .findFirst();

      if (mapping.isPresent()) {
        FieldJoinMapping m = mapping.get();
        f.setJoinTable(m.joinTable());
        f.setJoinField(m.targetColumn());
        f.setJoinType(m.joinType());
      }
    }
    return basicFilters;
  }

  public List<FieldJoinMapping> extractMappingProperty() {
    List<FieldJoinMapping> result = new ArrayList<>();

    if (this.clazz == null) {
      return result;
    }

    Field[] fields = this.clazz.getDeclaredFields();

    for (Field f : fields) {
      if (f.isAnnotationPresent(SpecificationMapping.class)) {
        SpecificationMapping annotation = f.getAnnotation(SpecificationMapping.class);

        if (!StringUtils.isEmpty(annotation.joinTable()) && !StringUtils.isEmpty(annotation.targetColumn())) {
          result.add(new FieldJoinMapping(
              annotation.joinTable(),
              annotation.targetColumn(),
              annotation.joinType().name(),
              f.getName()));
        }
      }
    }
    return result;
  }

}
