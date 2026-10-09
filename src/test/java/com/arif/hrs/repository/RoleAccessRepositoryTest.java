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
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class RoleAccessRepositoryTest {

  @Autowired
  private RoleAccessRepository roleAccessRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AccessRepository accessRepository;

  @BeforeEach
  void setupData() {
    RoleModel adminRole = roleRepository.save(roleModel("Admin", "ADMIN"));
    RoleModel userRole = roleRepository.save(roleModel("User", "USER"));
    AccessModel readAccess = accessRepository.save(accessModel("Read", "GET", "/read"));
    AccessModel writeAccess = accessRepository.save(accessModel("Write", "POST", "/write"));

    roleAccessRepository.save(roleAccessModel(adminRole, readAccess));
    roleAccessRepository.save(roleAccessModel(adminRole, writeAccess));
    roleAccessRepository.save(roleAccessModel(userRole, readAccess));
  }

  @AfterEach
  void cleanupData() {
    roleAccessRepository.deleteAll();
    accessRepository.deleteAll();
    roleRepository.deleteAll();
  }

  @Test
  void filterRoleAccessUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("roleName", "Admin", new ArrayList<>(), "EQ", "AND"),
        filterDto("accessName", "Read", new ArrayList<>(), "EQ", "AND")
    });

    Specification<RoleAccessModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        RoleAccessDto.class);
    List<RoleAccessModel> result = roleAccessRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterRoleAccessUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("roleName", "Admin", new ArrayList<>(), "EQ", "OR"),
        filterDto("accessName", "Write", new ArrayList<>(), "EQ", "OR")
    });

    Specification<RoleAccessModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        RoleAccessDto.class);
    List<RoleAccessModel> result = roleAccessRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static RoleModel roleModel(String name, String code) {
    RoleModel model = new RoleModel();
    model.setName(name);
    model.setCode(code);
    return model;
  }

  private static AccessModel accessModel(String name, String method, String path) {
    AccessModel model = new AccessModel();
    model.setName(name);
    model.setMethod(method);
    model.setPath(path);
    return model;
  }

  private static RoleAccessModel roleAccessModel(RoleModel role, AccessModel access) {
    RoleAccessModel model = new RoleAccessModel();
    model.setRole(role);
    model.setAccess(access);
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
