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
import com.arif.hrs.domain.dto.ShiftDto;
import com.arif.hrs.model.ShiftModel;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class ShiftRepositoryTest {

  @Autowired
  private ShiftRepository shiftRepository;

  @Autowired
  private ShiftTypeRepository shiftTypeRepository;

  @Autowired
  private ShiftToleranceRepository shiftToleranceRepository;

  @BeforeEach
  void setupData() {
    ShiftTypeModel dayType = shiftTypeRepository.save(shiftTypeModel("DAY", "Day"));
    ShiftTypeModel nightType = shiftTypeRepository.save(shiftTypeModel("NIGHT", "Night"));
    ShiftToleranceModel fiveMinuteTolerance = shiftToleranceRepository
        .save(shiftToleranceModel("FIVE", "Five minutes", 5));
    ShiftToleranceModel tenMinuteTolerance = shiftToleranceRepository
        .save(shiftToleranceModel("TEN", "Ten minutes", 10));
    ShiftToleranceModel fifteenMinuteTolerance = shiftToleranceRepository
        .save(shiftToleranceModel("FIFTEEN", "Fifteen minutes", 15));

    shiftRepository.save(shiftModel("DAY_MORNING", "Morning", dayType, fiveMinuteTolerance,
        tenMinuteTolerance));
    shiftRepository.save(shiftModel("NIGHT_EVENING", "Evening", nightType, tenMinuteTolerance,
        fifteenMinuteTolerance));
    shiftRepository.save(shiftModel("NIGHT_GRAVEYARD", "Graveyard", nightType, fiveMinuteTolerance,
        fifteenMinuteTolerance));
  }

  @AfterEach
  void cleanupData() {
    shiftRepository.deleteAll();
    shiftTypeRepository.deleteAll();
    shiftToleranceRepository.deleteAll();
  }

  @Test
  void filterShiftUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("shiftTypeName", "Day", new ArrayList<>(), "EQ", "AND"),
        filterDto("inToleranceName", "Five minutes", new ArrayList<>(), "EQ", "AND")
    });

    Specification<ShiftModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, ShiftDto.class);
    List<ShiftModel> result = shiftRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterShiftUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("shiftName", "Morning", new ArrayList<>(), "LIKE", "OR"),
        filterDto("outToleranceName", "Fifteen minutes", new ArrayList<>(), "EQ", "OR")
    });

    Specification<ShiftModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, ShiftDto.class);
    List<ShiftModel> result = shiftRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(3, result.size());
  }

  private static ShiftTypeModel shiftTypeModel(String code, String name) {
    ShiftTypeModel model = new ShiftTypeModel();
    model.setCode(code);
    model.setName(name);
    return model;
  }

  private static ShiftToleranceModel shiftToleranceModel(String code, String name, int minutes) {
    ShiftToleranceModel model = new ShiftToleranceModel();
    model.setCode(code);
    model.setToleranceName(name);
    model.setToleranceBefore(minutes);
    model.setToleranceAfter(minutes);
    return model;
  }

  private static ShiftModel shiftModel(
      String code,
      String name,
      ShiftTypeModel type,
      ShiftToleranceModel inTolerance,
      ShiftToleranceModel outTolerance) {
    ShiftModel model = new ShiftModel();
    model.setCode(code);
    model.setShiftName(name);
    model.setShiftType(type);
    model.setInTolerance(inTolerance);
    model.setOutTolerance(outTolerance);
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
