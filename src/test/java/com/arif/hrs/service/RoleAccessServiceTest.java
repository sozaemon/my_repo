package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.mapper.RoleAccessMapper;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.repository.RoleAccessRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RoleAccessServiceTest {

  @Mock
  private RoleAccessRepository roleAccessRepository;

  @Mock
  private RoleAccessMapper mapper;

  @InjectMocks
  private RoleAccessServiceImpl roleAccessService;

  @Test
  void getOneRoleAccessReturnMappedRoleAccessFound() {
    RoleAccessModel model = roleAccessModel(1, 2, "ROLE_TEST", 3, "Access Test");
    RoleAccessDto dto = roleAccessDto(1, 2, "ROLE_TEST", 3, "Access Test");

    when(roleAccessRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<RoleAccessDto> result = roleAccessService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(roleAccessRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneRoleAccessReturnEmptyWhenRoleAccessDoesNotExists() {
    when(roleAccessRepository.findById(1)).thenReturn(Optional.empty());

    Optional<RoleAccessDto> result = roleAccessService.getOne(1);

    assertTrue(result.isEmpty());
    verify(roleAccessRepository).findById(1);
  }

  @Test
  void createMapAndSaveRoleAccess() {
    RoleAccessDto request = roleAccessDto(null, 2, "ROLE_TEST", 3, "Access Test");
    RoleAccessModel model = roleAccessModel(null, 2, "ROLE_TEST", 3, "Access Test");

    RoleAccessDto savedDto = roleAccessDto(1, 2, "ROLE_TEST", 3, "Access Test");
    RoleAccessModel savedModel = roleAccessModel(1, 2, "ROLE_TEST", 3, "Access Test");

    when(mapper.dtoToModel(request)).thenReturn(model);
    when(roleAccessRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    RoleAccessDto result = roleAccessService.create(request);
    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(roleAccessRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateExistingRoleAccess() {
    RoleAccessDto request = roleAccessDto(
        1, 2, "ROLE_TEST",
        3, "Access Test Updated");
    RoleAccessModel existing = roleAccessModel(
        1, 2, "ROLE_TEST",
        3, "Access Test");

    RoleAccessDto updatedDto = roleAccessDto(
        1, 2, "ROLE_TEST",
        3, "Access Test Updated");

    when(roleAccessRepository.findById(1)).thenReturn(Optional.of(existing));
    when(roleAccessRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    RoleAccessDto result = roleAccessService.update(request);

    assertEquals(updatedDto, result);
    assertEquals("Access Test Updated", updatedDto.getAccessName());

    verify(roleAccessRepository).findById(1);
    verify(roleAccessRepository).save(existing);

  }

  @Test
  void updateNotFoundRoleAccess() {
    RoleAccessDto request = roleAccessDto(
        1, 2, "ROLE_TEST",
        3, "Access Test Updated");

    when(roleAccessRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> roleAccessService.update(request));
    verify(roleAccessRepository).findById(1);

  }

  @Test
  void deleteExistingRoleAccess() {
    RoleAccessModel model = roleAccessModel(1, 1, "TEST ROLE", 1, "Test Access");
    when(roleAccessRepository.findById(1)).thenReturn(Optional.of(model));

    roleAccessService.delete(1);

    verify(roleAccessRepository).delete(model);
  }

  @Test
  void deleteNotExistingRoleAccess() {
    when(roleAccessRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> roleAccessService.delete(1));
    verify(roleAccessRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("accessName");
    request.setSortDirection("asc");

    RoleAccessModel model = roleAccessModel(1, 1, "TEST_ROLE",
        1, "Test Access");
    RoleAccessDto dto = roleAccessDto(1, 1, "TEST_ROLE",
        1, "Test Access");

    when(roleAccessRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<RoleAccessModel>>isNull(),
        any(Pageable.class))).thenReturn(
            new PageImpl<>(
                List.of(model),
                PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<RoleAccessDto> result = roleAccessService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(roleAccessRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<RoleAccessModel>>isNull(),
        pageableCaptor.capture());

    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("accessName", pageable.getSort().getOrderFor("accessName").getProperty());
  }

  @Test
  void assignRolesToAccessRemovesOutdatedAssignmentsAndCreatesMissingOnes() {
    Integer accessId = 10;
    RoleAccessModel outdatedAssignment = roleAccessModel(1, 1, "ROLE_OLD", accessId, "Test Access");
    RoleAccessModel existingAssignment = roleAccessModel(2, 2, "ROLE_EXISTING", accessId, "Test Access");
    RoleAccessModel newAssignment = roleAccessModel(3, 3, "ROLE_NEW", accessId, "Test Access");
    RoleAccessDto newAssignmentDto = roleAccessDto(3, 3, "ROLE_NEW", accessId, "Test Access");
    List<Integer> requestedRoleIds = List.of(2, 3);

    when(roleAccessRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<RoleAccessModel>>any()))
        .thenReturn(List.of(outdatedAssignment, existingAssignment));
    when(mapper.dtoToModel(any(RoleAccessDto.class))).thenReturn(newAssignment);
    when(roleAccessRepository.save(newAssignment)).thenReturn(newAssignment);
    when(mapper.modelToDto(newAssignment)).thenReturn(newAssignmentDto);

    List<RoleAccessDto> result = roleAccessService.assignRolesToAccess(requestedRoleIds, accessId);

    assertEquals(List.of(newAssignmentDto), result);
    verify(roleAccessRepository).delete(outdatedAssignment);
    verify(roleAccessRepository).save(newAssignment);
    verify(mapper).modelToDto(newAssignment);

    ArgumentCaptor<RoleAccessDto> dtoCaptor = ArgumentCaptor.forClass(RoleAccessDto.class);
    verify(mapper).dtoToModel(dtoCaptor.capture());
    assertEquals(3, dtoCaptor.getValue().getRoleId());
    assertEquals(accessId, dtoCaptor.getValue().getAccessId());
  }

  private static RoleAccessDto roleAccessDto(
      Integer id,
      Integer roleId,
      String roleName,
      Integer accessId,
      String accessName) {
    RoleAccessDto dto = new RoleAccessDto();
    dto.setId(id);
    dto.setAccessId(accessId);
    dto.setAccessName(accessName);
    dto.setRoleId(roleId);
    dto.setRoleName(roleName);

    return dto;
  }

  private static RoleAccessModel roleAccessModel(
      Integer id,
      Integer roleId,
      String roleName,
      Integer accessId,
      String accessName) {
    RoleModel role = roleModel(roleId, roleName);
    AccessModel access = accessModel(accessId, accessName);

    RoleAccessModel roleAccess = new RoleAccessModel();
    roleAccess.setId(id);
    roleAccess.setRole(role);
    roleAccess.setAccess(access);

    return roleAccess;
  }

  private static RoleModel roleModel(
      Integer id,
      String name) {
    RoleModel model = new RoleModel();
    model.setId(id);
    model.setName(name);

    return model;
  }

  private static AccessModel accessModel(
      Integer id,
      String name) {
    AccessModel model = new AccessModel();
    model.setId(id);
    model.setName(name);
    return model;
  }
}
