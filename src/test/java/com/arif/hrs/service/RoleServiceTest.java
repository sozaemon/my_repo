package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private RoleMapper mapper;

  @InjectMocks
  private RoleServiceImpl roleService;

  @Test
  void getOneReturnsMappedRoleWhenFound() {
    RoleModel model = roleModel(1, "ADMIN", "Administrator", "Admin role", true);
    RoleDto dto = roleDto(1, "ADMIN", "Administrator", "Admin role", true);
    when(roleRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<RoleDto> result = roleService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(roleRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsEmptyWhenRoleDoesNotExist() {
    when(roleRepository.findById(1)).thenReturn(Optional.empty());

    Optional<RoleDto> result = roleService.getOne(1);

    assertTrue(result.isEmpty());
    verify(roleRepository).findById(1);
  }

  @Test
  void createMapsAndSavesRole() {
    RoleDto request = roleDto(null, "ADMIN", "Administrator", "Admin role", true);
    RoleModel model = roleModel(null, "ADMIN", "Administrator", "Admin role", true);
    RoleModel savedModel = roleModel(1, "ADMIN", "Administrator", "Admin role", true);
    RoleDto savedDto = roleDto(1, "ADMIN", "Administrator", "Admin role", true);
    when(mapper.dtoToModel(request)).thenReturn(model);
    when(roleRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    RoleDto result = roleService.create(request);

    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(roleRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateChangesAndSavesExistingRole() {
    RoleDto request = roleDto(1, "ADMIN", "Updated administrator", "Updated role", false);
    RoleModel existing = roleModel(1, "ADMIN", "Administrator", "Admin role", true);
    RoleDto updatedDto = roleDto(1, "ADMIN", "Updated administrator", "Updated role", false);
    when(roleRepository.findById(1)).thenReturn(Optional.of(existing));
    when(roleRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    RoleDto result = roleService.update(request);

    assertEquals(updatedDto, result);
    assertEquals("Updated administrator", existing.getName());
    assertEquals("Updated role", existing.getDescription());
    assertEquals(false, existing.getActive());
    verify(roleRepository).findById(1);
    verify(roleRepository).save(existing);
    verify(mapper).modelToDto(existing);
  }

  @Test
  void updateThrowsWhenRoleDoesNotExist() {
    RoleDto request = roleDto(1, "ADMIN", "Administrator", "Admin role", true);
    when(roleRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> roleService.update(request));

    verify(roleRepository).findById(1);
  }

  @Test
  void deleteRemovesExistingRole() {
    RoleModel model = roleModel(1, "ADMIN", "Administrator", "Admin role", true);
    when(roleRepository.findById(1)).thenReturn(Optional.of(model));

    roleService.delete(1);

    verify(roleRepository).findById(1);
    verify(roleRepository).delete(model);
  }

  @Test
  void deleteThrowsWhenRoleDoesNotExist() {
    when(roleRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> roleService.delete(1));

    verify(roleRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndUsesRequestedPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("name");
    request.setSortDirection("asc");
    RoleModel model = roleModel(1, "ADMIN", "Administrator", "Admin role", true);
    RoleDto dto = roleDto(1, "ADMIN", "Administrator", "Admin role", true);
    when(roleRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<RoleModel>>isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(model), PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<RoleDto> result = roleService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(roleRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<RoleModel>>isNull(), pageableCaptor.capture());
    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("name", pageable.getSort().getOrderFor("name").getProperty());
  }

  private static RoleModel roleModel(
      Integer id,
      String code,
      String name,
      String description,
      Boolean active) {
    RoleModel model = new RoleModel();
    model.setId(id);
    model.setCode(code);
    model.setName(name);
    model.setDescription(description);
    model.setActive(active);
    return model;
  }

  private static RoleDto roleDto(
      Integer id,
      String code,
      String name,
      String description,
      Boolean active) {
    RoleDto dto = new RoleDto();
    dto.setId(id);
    dto.setCode(code);
    dto.setName(name);
    dto.setDescription(description);
    dto.setActive(active);
    return dto;
  }
}
