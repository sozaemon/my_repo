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

import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.domain.dto.FilterDto;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class AccessRepositoryTest {

  @Autowired
  private AccessRepository accessRepository;

  private AccessModel[] sourceData = new AccessModel[] {
      accessModel("Test Access 1", "GET", "/test/test-1", "test access 1"),
      accessModel("Test Access 2", "POST", "/test/test-2", "test access 2"),
      accessModel("Test Access 3", "PUT", "/test/test-3", "test access 3"),
      accessModel("Test Access 4", "PATCH", "/test/test-4", "test access 4"),
      accessModel("Test Access 5", "DELETE", "/test/test-5", "test access 5")
  };

  @BeforeEach
  void setupData() {
    for (AccessModel m : sourceData) {
      accessRepository.save(m);
    }
  }

  @AfterEach
  void cleanupData() {
    List<AccessModel> data = accessRepository.findAll();
    for (AccessModel m : data) {
      accessRepository.delete(m);
    }
  }

  @Test
  void filterAccessUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "1", new ArrayList<>(), "LIKE", "AND"),
        filterDto("method", "GET", new ArrayList<>(), "EQ", "AND")
    });

    Specification<AccessModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, AccessDto.class);
    List<AccessModel> result = accessRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterAccessUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "1", new ArrayList<>(), "LIKE", "OR"),
        filterDto("method", "DELETE", new ArrayList<>(), "EQ", "OR")
    });

    Specification<AccessModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, AccessDto.class);
    List<AccessModel> result = accessRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static AccessModel accessModel(
      String name,
      String method,
      String path,
      String description) {
    AccessModel model = new AccessModel();
    model.setName(name);
    model.setMethod(method);
    model.setPath(path);
    model.setDescription(description);

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
