package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.arif.hrs.domain.dto.AssignUserRoleDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.mapper.UserMapper;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.model.UserRoleModel;
import com.arif.hrs.repository.RoleRepository;
import com.arif.hrs.repository.UserRepository;
import com.arif.hrs.repository.UserRoleRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserRoleRepository userRoleRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private UserMapper mapper;

  @Mock
  private RoleMapper roleMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void getRolesByUserIdReturnsMappedRoles() {
    RoleModel adminRole = roleModel(1, "ADMIN", "Administrator");
    RoleModel userRole = roleModel(2, "USER", "Regular User");
    UserModel user = userModel(1);
    user.setUserRoles(Set.of(userRoleModel(user, adminRole), userRoleModel(user, userRole)));
    RoleDto adminDto = roleDto(1, "ADMIN", "Administrator");
    RoleDto userDto = roleDto(2, "USER", "Regular User");

    when(userRepository.findById(1)).thenReturn(Optional.of(user));
    when(roleMapper.modelToDto(adminRole)).thenReturn(adminDto);
    when(roleMapper.modelToDto(userRole)).thenReturn(userDto);

    List<RoleDto> result = userService.getRolesByUserId(1);

    assertEquals(2, result.size());
    assertTrue(result.containsAll(List.of(adminDto, userDto)));
    verify(userRepository).findById(1);
    verify(roleMapper).modelToDto(adminRole);
    verify(roleMapper).modelToDto(userRole);
  }

  @Test
  void getRolesByUserIdReturnsEmptyListWhenUserHasNoRoles() {
    UserModel user = userModel(1);

    when(userRepository.findById(1)).thenReturn(Optional.of(user));

    List<RoleDto> result = userService.getRolesByUserId(1);

    assertTrue(result.isEmpty());
    verify(userRepository).findById(1);
  }

  @Test
  void getRolesByUserIdThrowsWhenUserDoesNotExist() {
    when(userRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.getRolesByUserId(99));

    verify(userRepository).findById(99);
  }

  @Test
  void assignUserRoleRemovesUnrequestedAndAddsMissingRoles() {
    UserModel user = userModel(1);
    RoleModel adminRole = roleModel(1, "ADMIN", "Administrator");
    RoleModel userRole = roleModel(2, "USER", "Regular User");
    RoleModel auditorRole = roleModel(3, "AUDITOR", "Auditor");
    List<UserRoleModel> existing = List.of(userRoleModel(user, adminRole), userRoleModel(user, userRole));
    AssignUserRoleDto request = new AssignUserRoleDto();
    request.setUserId(1);
    request.setRoleId(List.of(2, 3));

    when(userRepository.findById(1)).thenReturn(Optional.of(user));
    when(userRoleRepository.findByUserId(1)).thenReturn(existing);
    when(roleRepository.findAllById(List.of(3))).thenReturn(List.of(auditorRole));

    userService.assignUserRole(request);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<UserRoleModel>> removedCaptor = ArgumentCaptor.forClass(List.class);
    verify(userRoleRepository).deleteAll(removedCaptor.capture());
    assertEquals(List.of(1), removedCaptor.getValue().stream()
        .map(existingUserRole -> existingUserRole.getRole().getId())
        .toList());

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<UserRoleModel>> addedCaptor = ArgumentCaptor.forClass(List.class);
    verify(userRoleRepository).saveAll(addedCaptor.capture());
    List<UserRoleModel> added = addedCaptor.getValue();
    assertEquals(1, added.size());
    assertEquals(3, added.get(0).getRole().getId());
    assertEquals(user, added.get(0).getUser());
  }

  @Test
  void assignUserRoleThrowsWhenUserDoesNotExist() {
    AssignUserRoleDto request = new AssignUserRoleDto();
    request.setUserId(99);
    request.setRoleId(List.of(1));

    when(userRepository.findById(99)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.assignUserRole(request));

    verify(userRepository).findById(99);
  }

  private static UserModel userModel(Integer id) {
    UserModel user = new UserModel();
    user.setId(id);
    user.setUserName("arif");
    return user;
  }

  private static UserRoleModel userRoleModel(UserModel user, RoleModel role) {
    UserRoleModel userRole = new UserRoleModel();
    userRole.setUser(user);
    userRole.setRole(role);
    return userRole;
  }

  private static RoleModel roleModel(Integer id, String code, String name) {
    RoleModel role = new RoleModel();
    role.setId(id);
    role.setCode(code);
    role.setName(name);
    role.setDescription(name);
    role.setActive(true);
    return role;
  }

  private static RoleDto roleDto(Integer id, String code, String name) {
    RoleDto dto = new RoleDto();
    dto.setId(id);
    dto.setCode(code);
    dto.setName(name);
    dto.setDescription(name);
    dto.setActive(true);
    return dto;
  }
}
