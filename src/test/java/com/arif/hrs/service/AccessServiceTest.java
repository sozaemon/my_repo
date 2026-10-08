package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.mapper.AccessMapper;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.repository.AccessRepository;
import com.arif.hrs.util.excel.excelmodel.AccessExcelModel;

@ExtendWith(MockitoExtension.class)
class AccessServiceTest {

  @Mock
  private AccessRepository accessRepository;

  @Mock
  private AccessMapper mapper;

  @InjectMocks
  private AccessServiceImpl accessService;

  @Test
  void getAccessByRolesAndPathAndMethodReturnsMappedAccesses() {
    List<String> roles = List.of("ADMIN");
    AccessModel model = accessModel(1, "View employees", "/employees", "GET", "List employees");
    AccessDto dto = accessDto(1, "View employees", "/employees", "GET", "List employees");
    when(accessRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<AccessModel>>any())).thenReturn(List.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    List<AccessDto> result = accessService.getAccessByRolesAndPathAndMethod(roles, "GET");

    assertEquals(List.of(dto), result);
    verify(accessRepository).findAll(org.mockito.ArgumentMatchers.<Specification<AccessModel>>any());
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsMappedAccessWhenFound() {
    AccessModel model = accessModel(1, "View employees", "/employees", "GET", "List employees");
    AccessDto dto = accessDto(1, "View employees", "/employees", "GET", "List employees");
    when(accessRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<AccessDto> result = accessService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(accessRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsEmptyWhenAccessDoesNotExist() {
    when(accessRepository.findById(1)).thenReturn(Optional.empty());

    Optional<AccessDto> result = accessService.getOne(1);

    assertTrue(result.isEmpty());
    verify(accessRepository).findById(1);
  }

  @Test
  void createMapsAndSavesAccess() {
    AccessDto request = accessDto(null, "View employees", "/employees", "GET", "List employees");
    AccessModel model = accessModel(null, "View employees", "/employees", "GET", "List employees");
    AccessDto savedDto = accessDto(1, "View employees", "/employees", "GET", "List employees");
    AccessModel savedModel = accessModel(1, "View employees", "/employees", "GET", "List employees");
    when(mapper.dtoToModel(request)).thenReturn(model);
    when(accessRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    AccessDto result = accessService.create(request);

    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(accessRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateChangesAndSavesExistingAccess() {
    AccessDto request = accessDto(1, "Updated name", "/employees", "POST", "Updated description");
    AccessModel existing = accessModel(1, "Old name", "/employees", "GET", "Old description");
    AccessDto updatedDto = accessDto(1, "Updated name", "/employees", "POST", "Updated description");
    when(accessRepository.findById(1)).thenReturn(Optional.of(existing));
    when(accessRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    AccessDto result = accessService.update(request);

    assertEquals(updatedDto, result);
    assertEquals("Updated name", existing.getName());
    assertEquals("POST", existing.getMethod());
    assertEquals("Updated description", existing.getDescription());
    verify(accessRepository).save(existing);
    verify(mapper).modelToDto(existing);
  }

  @Test
  void updateThrowsWhenAccessDoesNotExist() {
    AccessDto request = accessDto(1, "Updated name", "/employees", "POST", "Updated description");
    when(accessRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> accessService.update(request));

    verify(accessRepository).findById(1);
  }

  @Test
  void deleteRemovesExistingAccess() {
    AccessModel model = accessModel(1, "View employees", "/employees", "GET", "List employees");
    when(accessRepository.findById(1)).thenReturn(Optional.of(model));

    accessService.delete(1);

    verify(accessRepository).delete(model);
  }

  @Test
  void deleteThrowsWhenAccessDoesNotExist() {
    when(accessRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> accessService.delete(1));

    verify(accessRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndUsesRequestedPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("name");
    request.setSortDirection("asc");
    AccessModel model = accessModel(1, "View employees", "/employees", "GET", "List employees");
    AccessDto dto = accessDto(1, "View employees", "/employees", "GET", "List employees");
    when(accessRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<AccessModel>>isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(model), PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<AccessDto> result = accessService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(accessRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<AccessModel>>isNull(), pageableCaptor.capture());
    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("name", pageable.getSort().getOrderFor("name").getProperty());
  }

  @Test
  void exportToExcelReturnsWorkbookBytes() {
    AccessModel model = accessModel(1, "View employees", "/employees", "GET", "List employees");
    AccessExcelModel excelModel = new AccessExcelModel();
    excelModel.setName("View employees");
    excelModel.setPath("/employees");
    excelModel.setMethod("GET");
    excelModel.setDescription("List employees");
    when(accessRepository.findAll()).thenReturn(List.of(model));
    when(mapper.modelToExcel(model)).thenReturn(excelModel);

    ByteArrayInputStream result = accessService.exportToExcel();

    assertNotNull(result);
    assertTrue(result.available() > 0);
    verify(accessRepository).findAll();
    verify(mapper).modelToExcel(model);
  }

  private static AccessModel accessModel(
      Integer id,
      String name,
      String path,
      String method,
      String description) {
    AccessModel model = new AccessModel();
    model.setId(id);
    model.setName(name);
    model.setPath(path);
    model.setMethod(method);
    model.setDescription(description);
    return model;
  }

  private static AccessDto accessDto(
      Integer id,
      String name,
      String path,
      String method,
      String description) {
    AccessDto dto = new AccessDto();
    dto.setId(id);
    dto.setName(name);
    dto.setPath(path);
    dto.setMethod(method);
    dto.setDescription(description);
    return dto;
  }
}
