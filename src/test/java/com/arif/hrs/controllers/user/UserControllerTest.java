package com.arif.hrs.controllers.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.arif.hrs.controllers.advice.AdviceController;
import com.arif.hrs.controllers.httpmodel.AssignUserRole;
import com.arif.hrs.controllers.httpmodel.Role;
import com.arif.hrs.domain.dto.AssignUserRoleDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.domain.service.UserAuthServiceDomain;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.mapper.AssignUserRoleMapper;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserAuthServiceDomain userAuthService;

  @Mock
  private UserServiceDomain userService;

  @Mock
  private UserMapper userMapper;

  @Mock
  private RoleServiceDomain roleService;

  @Mock
  private RoleMapper roleMapper;

  @Mock
  private AssignUserRoleMapper assignUserRoleMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    UserController controller = new UserController(userAuthService, userService, userMapper, roleService,
        roleMapper, assignUserRoleMapper);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new AdviceController())
        .build();
  }

  @Test
  void getUserRolesByUserIdReturnsRolesForUser() throws Exception {
    RoleDto adminDto = roleDto(1, "ADMIN", "Administrator");
    Role httpRole = role(2, "USER", "Regular User");

    when(userService.getRolesByUserId(7)).thenReturn(List.of(adminDto));
    when(roleMapper.dtoToHttp(adminDto)).thenReturn(httpRole);

    mockMvc.perform(get("/api/user/user-roles/7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("success to fetch user roles"))
        .andExpect(jsonPath("$.data.length()").value(1))
        .andExpect(jsonPath("$.data[0].id").value(2))
        .andExpect(jsonPath("$.data[0].code").value("USER"))
        .andExpect(jsonPath("$.data[0].name").value("Regular User"));

    verify(userService).getRolesByUserId(7);
    verify(roleMapper).dtoToHttp(adminDto);
  }

  @Test
  void getUserRolesByUserIdReturnsEmptyListWhenUserHasNoRoles() throws Exception {
    when(userService.getRolesByUserId(7)).thenReturn(List.of());

    mockMvc.perform(get("/api/user/user-roles/7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(0));

    verify(userService).getRolesByUserId(7);
  }

  @Test
  void assignUserRoleCallsServiceWithMappedDto() throws Exception {
    AssignUserRoleDto dto = new AssignUserRoleDto();
    dto.setUserId(7);
    dto.setRoleId(List.of(2, 3));
    when(assignUserRoleMapper.httpToDto(any(AssignUserRole.class))).thenReturn(dto);

    mockMvc.perform(post("/api/user/assign-role")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"userId\":7,\"roleId\":[2,3]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Success to assign user role"))
        .andExpect(jsonPath("$.data").value("Success"));

    verify(userService).assignUserRole(dto);
    ArgumentCaptor<AssignUserRole> captor = ArgumentCaptor.forClass(AssignUserRole.class);
    verify(assignUserRoleMapper).httpToDto(captor.capture());
    assertEquals(7, captor.getValue().getUserId());
    assertEquals(List.of(2, 3), captor.getValue().getRoleId());
  }

  @Test
  void assignUserRoleReturnsBadRequestWhenUserIdMissing() throws Exception {
    mockMvc.perform(post("/api/user/assign-role")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"roleId\":[2,3]}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("validation failed"));

    verifyNoInteractions(userService, assignUserRoleMapper);
  }

  @Test
  void assignUserRoleReturnsBadRequestWhenRoleIdIsEmpty() throws Exception {
    mockMvc.perform(post("/api/user/assign-role")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"userId\":7,\"roleId\":[]}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("validation failed"));

    verifyNoInteractions(userService, assignUserRoleMapper);
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

  private static Role role(Integer id, String code, String name) {
    Role role = new Role();
    role.setId(id);
    role.setCode(code);
    role.setName(name);
    role.setDescription(name);
    role.setActive(true);
    return role;
  }
}
