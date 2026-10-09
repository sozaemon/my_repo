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
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class RoleRepositoryTest {

  @Autowired
  private RoleRepository roleRepository;

  private final RoleModel[] sourceData = new RoleModel[] {
      roleModel("Administrator", "ADMIN", true),
      roleModel("User", "USER", true),
      roleModel("Test Role 3", "TEST_3", false),
      roleModel("Test Role 4", "TEST_4", true),
      roleModel("Test Role 5", "TEST_5", false)
  };

  @BeforeEach
  void setupData() {
    for (RoleModel model : sourceData) {
      roleRepository.save(model);
    }
  }

  @AfterEach
  void cleanupData() {
    roleRepository.deleteAll();
  }

  @Test
  void filterRoleUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "Admin", new ArrayList<>(), "LIKE", "AND"),
        filterDto("active", true, new ArrayList<>(), "EQ", "AND")
    });

    Specification<RoleModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, RoleDto.class);
    List<RoleModel> result = roleRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterRoleUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("name", "Admin", new ArrayList<>(), "LIKE", "OR"),
        filterDto("code", "USER", new ArrayList<>(), "EQ", "OR")
    });

    Specification<RoleModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, RoleDto.class);
    List<RoleModel> result = roleRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static RoleModel roleModel(String name, String code, boolean active) {
    RoleModel model = new RoleModel();
    model.setName(name);
    model.setCode(code);
    model.setActive(active);
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
