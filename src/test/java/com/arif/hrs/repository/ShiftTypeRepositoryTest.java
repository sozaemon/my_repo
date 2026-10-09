package com.arif.hrs.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.domain.dto.FilterDto;
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class ShiftTypeRepositoryTest {

  @Autowired
  private ShiftTypeRepository shiftTypeRepository;

  private final ShiftTypeModel[] sourceData = new ShiftTypeModel[] {
      shiftTypeModel("DAY", "Day"),
      shiftTypeModel("NIGHT", "Night"),
      shiftTypeModel("FLEX", "Flexible"),
      shiftTypeModel("SPLIT", "Split"),
      shiftTypeModel("ROTATING", "Rotating")
  };

  @BeforeEach
  void setupData() {
    for (ShiftTypeModel model : sourceData) {
      shiftTypeRepository.save(model);
    }
  }

  @AfterEach
  void cleanupData() {
    shiftTypeRepository.deleteAll();
  }

  @Test
  void filterShiftTypeUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "Day", new ArrayList<>(), "EQ", "AND"),
        filterDto("code", "DAY", new ArrayList<>(), "EQ", "AND")
    });

    Specification<ShiftTypeModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        ShiftTypeDto.class);
    List<ShiftTypeModel> result = shiftTypeRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterShiftTypeUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "Day", new ArrayList<>(), "EQ", "OR"),
        filterDto("code", "NIGHT", new ArrayList<>(), "EQ", "OR")
    });

    Specification<ShiftTypeModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        ShiftTypeDto.class);
    List<ShiftTypeModel> result = shiftTypeRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static ShiftTypeModel shiftTypeModel(String code, String name) {
    ShiftTypeModel model = new ShiftTypeModel();
    model.setCode(code);
    model.setName(name);
    return model;
  }

  private static FilterDto filterDto(
      String fieldName,
      Object value,
      List<Object> values,
      String operator,
      String joinOperator) {
    FilterDto filter = new FilterDto();
    filter.setFieldName(fieldName);
    filter.setValue(value);
    filter.setValues(values);
    filter.setOperator(operator);
    filter.setJoinOperator(joinOperator);
    return filter;
  }
}
