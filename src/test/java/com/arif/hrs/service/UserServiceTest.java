package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.mapper.UserMapper;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.model.UserRoleModel;
import com.arif.hrs.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void loadUserByUsernameReturnsUserDetailsWithUserRoles() {
    UserModel user = userModel("arif", "encoded-password");
    RoleModel role = new RoleModel();
    role.setName("ADMIN");
    UserRoleModel userRole = new UserRoleModel();
    userRole.setUser(user);
    userRole.setRole(role);
    user.setUserRoles(Set.of(userRole));
    when(userRepository.findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any()))
        .thenReturn(Optional.of(user));

    UserDetails result = userService.loadUserByUsername("arif");

    assertEquals("arif", result.getUsername());
    assertEquals("encoded-password", result.getPassword());
    assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
    verify(userRepository).findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any());
  }

  @Test
  void loadUserByUsernameThrowsWhenUserDoesNotExist() {
    when(userRepository.findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any()))
        .thenReturn(Optional.empty());

    UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> userService.loadUserByUsername("missing"));

    assertEquals("cannot find user", exception.getMessage());
    verify(userRepository).findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any());
  }

  @Test
  void findUserByUserNameReturnsMappedUserWhenFound() {
    UserModel user = userModel("arif", "encoded-password");
    UserDto dto = userDto(1, "arif", "arif@example.com", "Arif");
    when(userRepository.findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any()))
        .thenReturn(Optional.of(user));
    when(mapper.modelToDto(user)).thenReturn(dto);

    Optional<UserDto> result = userService.findUserByUserName("arif");

    assertEquals(Optional.of(dto), result);
    verify(userRepository).findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any());
    verify(mapper).modelToDto(user);
  }

  @Test
  void findUserByUserNameReturnsEmptyWhenUserDoesNotExist() {
    when(userRepository.findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any()))
        .thenReturn(Optional.empty());

    Optional<UserDto> result = userService.findUserByUserName("missing");

    assertTrue(result.isEmpty());
    verify(userRepository).findOne(
        org.mockito.ArgumentMatchers.<Specification<UserModel>>any());
  }

  private static UserModel userModel(String userName, String password) {
    UserModel user = new UserModel();
    user.setId(1);
    user.setUserName(userName);
    user.setPassword(password);
    user.setEmail("arif@example.com");
    user.setFullName("Arif");
    return user;
  }

  private static UserDto userDto(
      Integer id,
      String userName,
      String email,
      String fullName) {
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setUserName(userName);
    dto.setEmail(email);
    dto.setFullName(fullName);
    return dto;
  }
}
