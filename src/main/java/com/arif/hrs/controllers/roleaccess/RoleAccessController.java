package com.arif.hrs.controllers.roleaccess;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.AssignRoleAccessRequest;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.RoleAccess;
import com.arif.hrs.controllers.util.ControllerUtil;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.domain.service.RoleAccessServiceDomain;
import com.arif.hrs.mapper.RoleAccessMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role-access")
@Slf4j
public class RoleAccessController {

  private final RoleAccessServiceDomain roleAccessService;
  private final RoleAccessMapper mapper;

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<PageResponse<RoleAccess>>> listRoleAccess(
      @RequestBody PaginationRequest request) {
    log.info("list role access");

    PageDto<RoleAccessDto> result = roleAccessService.paginate(new PaginationDto(request));

    List<RoleAccess> roleAccess = result.getData().stream()
        .map(mapper::dtoToHttp).toList();
    PageResponse<RoleAccess> response = new PageResponse<>(result, roleAccess);

    return ControllerUtil.createSuccessResponse(response, "success to fetch role access");
  }

  @GetMapping("/list/access/{accessId}")
  public ResponseEntity<ResponseModel<List<RoleAccess>>> listRoleAccessByAccessId(
      @PathVariable("accessId") Integer accessId) {

    List<RoleAccessDto> roleAccessDto = roleAccessService.findByAccessId(accessId);

    List<RoleAccess> roleAccess = roleAccessDto.stream().map(mapper::dtoToHttp).toList();

    return ControllerUtil.createSuccessResponse(roleAccess, "success to fetch role access");
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<RoleAccess>> createRoleAccess(
      @RequestBody RoleAccess request) {
    log.info("create role access");

    RoleAccessDto dto = roleAccessService.create(mapper.httpToDto(request));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(dto), "success to create role access");

  }

  @DeleteMapping("/delete/{roleAccessId}")
  public ResponseEntity<ResponseModel<String>> deleteRoleAccess(@PathVariable("roleAccessId") Integer roleAccessId) {
    roleAccessService.delete(roleAccessId);

    return ControllerUtil.createSuccessResponse("Success", "success to assign roles access");
  }

  @PostMapping("/assign-roles")
  public ResponseEntity<ResponseModel<List<RoleAccess>>> assignRoles(@RequestBody AssignRoleAccessRequest request) {
    log.info("assign roles {} to access {}", request.getRoleIds(), request.getAccessId());

    List<RoleAccessDto> roleAccess = roleAccessService.assignRolesToAccess(request.getRoleIds(), request.getAccessId());
    List<RoleAccess> result = roleAccess.stream().map(mapper::dtoToHttp).toList();

    return ControllerUtil.createSuccessResponse(result, "success to assign roles access");
  }
}
