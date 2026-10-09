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
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private final UserModel[] sourceData = new UserModel[] {
      userModel("admin", "Administrator", "admin@example.test", true),
      userModel("jdoe", "Jane Doe", "jane@example.test", true),
      userModel("asmith", "Alex Smith", "alex@example.test", false),
      userModel("bwayne", "Bruce Wayne", "bruce@example.test", true),
      userModel("ckent", "Clark Kent", "clark@example.test", false)
  };

  @BeforeEach
  void setupData() {
    for (UserModel model : sourceData) {
      userRepository.save(model);
    }
  }

  @AfterEach
  void cleanupData() {
    userRepository.deleteAll();
  }

  @Test
  void filterUserUsingSpecificationBuildFromDTO() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("fullName", "Jane", new ArrayList<>(), "LIKE", "AND"),
        filterDto("email", "jane@example.test", new ArrayList<>(), "EQ", "AND")
    });

    Specification<UserModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, UserDto.class);
    List<UserModel> result = userRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void filterUserUsingSpecificationBuildFromDTOWithOrJoinOperator() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("userName", "admin", new ArrayList<>(), "EQ", "OR"),
        filterDto("fullName", "Clark Kent", new ArrayList<>(), "EQ", "OR")
    });

    Specification<UserModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs, UserDto.class);
    List<UserModel> result = userRepository.findAll(specification);

    assertNotNull(result);
    assertEquals(2, result.size());
  }

  private static UserModel userModel(String userName, String fullName, String email, boolean active) {
    UserModel model = new UserModel();
    model.setUserName(userName);
    model.setFullName(fullName);
    model.setEmail(email);
    model.setPassword("test-password");
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
