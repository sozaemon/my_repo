package com.arif.hrs.controllers.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.Role;
import com.arif.hrs.controllers.util.ControllerUtil;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RoleServiceDomain roleService;
  private final RoleMapper mapper;

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<PageResponse<Role>>> listRole(
      @RequestBody PaginationRequest request) {
    log.info("list role");

    PageDto<RoleDto> result = roleService.paginate(new PaginationDto(request));

    List<Role> role = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<Role> response = new PageResponse<>(result, role);

    return ControllerUtil.createSuccessResponse(response, "success to fetch role");
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<Role>> createRole(@RequestBody Role role) {
    log.info("create role");

    RoleDto dtoResult = roleService.create(mapper.httpToDto(role));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(dtoResult), "success to create role");
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseModel<Role>> updateRole(@RequestBody Role role) {
    log.info("update role");

    RoleDto dtoResult = roleService.update(mapper.httpToDto(role));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(dtoResult), "success to update role");
  }

  @DeleteMapping("/delete/{roleId}")
  public ResponseEntity<ResponseModel<String>> deleteRole(@PathVariable("roleId") Integer roleId) {
    log.info("delete role");

    roleService.delete(roleId);

    return ControllerUtil.createSuccessResponse("success", "role deleted");
  }
}
