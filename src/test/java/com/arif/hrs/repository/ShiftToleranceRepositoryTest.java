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
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.model.enums.ToleranceUnitEnum;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class ShiftToleranceRepositoryTest {

  @Autowired
  private ShiftToleranceRepository shiftToleranceRepository;

  private final ShiftToleranceModel[] sourceData = new ShiftToleranceModel[] {
      shiftToleranceModel("FIVE", "Five minutes", 5),
      shiftToleranceModel("TEN", "Ten minutes", 10),
      shiftToleranceModel("FIFTEEN", "Fifteen minutes", 15),
      shiftToleranceModel("HOUR", "One hour", 60),
      shiftToleranceModel("HALF_HOUR", "Half hour", 30)
  };

  @BeforeEach
  void setupData() {
    for (ShiftToleranceModel model : sourceData) {
      shiftToleranceRepository.save(model);
    }
  }

  @AfterEach
  void cleanupData() {
    shiftToleranceRepository.deleteAll();
  }

  @Test
  void filterShiftToleranceUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("toleranceName", "Five", new ArrayList<>(), "LIKE", "AND"),
        filterDto("toleranceBefore", 5, new ArrayList<>(), "EQ", "AND")
    });

    Specification<ShiftToleranceModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        ShiftToleranceDto.class);
    List<ShiftToleranceModel> result = shiftToleranceRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterShiftToleranceUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("toleranceName", "Five minutes", new ArrayList<>(), "EQ", "OR"),
        filterDto("code", "TEN", new ArrayList<>(), "EQ", "OR")
    });

    Specification<ShiftToleranceModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        ShiftToleranceDto.class);
    List<ShiftToleranceModel> result = shiftToleranceRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static ShiftToleranceModel shiftToleranceModel(String code, String name, int minutes) {
    ShiftToleranceModel model = new ShiftToleranceModel();
    model.setCode(code);
    model.setToleranceName(name);
    model.setToleranceBeforeUnit(ToleranceUnitEnum.MINUTE);
    model.setToleranceAfterUnit(ToleranceUnitEnum.MINUTE);
    model.setToleranceBefore(minutes);
    model.setToleranceAfter(minutes);
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
