package com.arif.hrs.controllers.role;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.arif.hrs.controllers.advice.AdviceController;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.mapper.RoleMapper;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

  @Mock
  private RoleServiceDomain roleService;

  @Mock
  private RoleMapper roleMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RoleController controller = new RoleController(roleService, roleMapper);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new AdviceController())
        .build();
  }

  @Test
  void createRoleReturnsBadRequestWhenCodeMissing() throws Exception {
    mockMvc.perform(post("/api/role/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"Administrator\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("validation failed"));

    verifyNoInteractions(roleService, roleMapper);
  }

  @Test
  void updateRoleReturnsBadRequestWhenIdMissing() throws Exception {
    mockMvc.perform(put("/api/role/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"code\":\"ADMIN\",\"name\":\"Administrator\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("validation failed"));

    verifyNoInteractions(roleService, roleMapper);
  }
}
