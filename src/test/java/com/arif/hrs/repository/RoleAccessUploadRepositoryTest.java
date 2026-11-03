package com.arif.hrs.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.domain.dto.FilterDto;
import com.arif.hrs.domain.dto.RoleAccessUploadDto;
import com.arif.hrs.model.RoleAccessUploadModel;
import com.arif.hrs.model.UploadHeaderModel;
import com.arif.hrs.model.UploadProperty;
import com.arif.hrs.model.enums.AccessMethodEnum;
import com.arif.hrs.model.enums.UploadHeaderStatusEnum;
import com.arif.hrs.model.enums.UploadStatusEnum;
import com.arif.hrs.service.serviceutility.CommonImpl;

@DataJpaTest
public class RoleAccessUploadRepositoryTest {

  @Autowired
  private RoleAccessUploadRepository roleAccessUploadRepository;

  @Autowired
  private UploadHeaderRepository uploadHeaderRepository;

  private UploadHeaderModel header;

  @BeforeEach
  void setupData() {
    header = uploadHeaderRepository.save(uploadHeaderModel());
    roleAccessUploadRepository.save(uploadModel("Read", "/read", AccessMethodEnum.GET, header));
    roleAccessUploadRepository.save(uploadModel("Write", "/write", AccessMethodEnum.POST, header));
  }

  @AfterEach
  void cleanupData() {
    roleAccessUploadRepository.deleteAll();
    uploadHeaderRepository.deleteAll();
  }

  @Test
  void filterUploadWithPaginationDoesNotFetchJoinInCountQuery() {
    List<FilterDto> filterDTOs = Arrays.asList(new FilterDto[] {
        filterDto("uploadHeaderId", header.getId(), "EQ", "AND")
    });

    Specification<RoleAccessUploadModel> specification = CommonImpl.buildFilterSpecificationDto(filterDTOs,
        RoleAccessUploadDto.class);
    Page<RoleAccessUploadModel> result = roleAccessUploadRepository.findAll(specification,
        PageRequest.of(0, 1));

    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getContent().size());
  }

  private static UploadHeaderModel uploadHeaderModel() {
    UploadHeaderModel model = new UploadHeaderModel();
    model.setModuleName("Role Access");
    model.setStatus(UploadHeaderStatusEnum.UPLOADED);
    model.setDescription("test upload");
    return model;
  }

  private static RoleAccessUploadModel uploadModel(String name, String path, AccessMethodEnum method,
      UploadHeaderModel header) {
    RoleAccessUploadModel model = new RoleAccessUploadModel();
    model.setName(name);
    model.setPath(path);
    model.setMethod(method);
    model.setRoles("ADMIN");
    model.setUploadHeader(header);

    UploadProperty property = new UploadProperty();
    property.setUploadStatus(UploadStatusEnum.VALID);
    model.setUploadProperty(property);

    return model;
  }

  private static FilterDto filterDto(String fieldName, Object value, String operator, String joinOperator) {
    FilterDto filter = new FilterDto();
    filter.setFieldName(fieldName);
    filter.setValue(value);
    filter.setValues(new ArrayList<>());
    filter.setOperator(operator);
    filter.setJoinOperator(joinOperator);
    return filter;
  }
}
