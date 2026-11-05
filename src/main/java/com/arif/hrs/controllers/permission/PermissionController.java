package com.arif.hrs.controllers.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.Permission;
import com.arif.hrs.controllers.util.ControllerUtil;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.PermissionDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.annotations.ValidationGroups;
import com.arif.hrs.domain.service.PermissionServiceDomain;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.mapper.PermissionMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/permission")
@Slf4j
public class PermissionController {

  private final PermissionServiceDomain permissionService;
  private final PermissionMapper mapper;
  private final RoleServiceDomain roleService;

  public PermissionController(PermissionServiceDomain permissionService, RoleServiceDomain roleService, PermissionMapper mapper) {
    this.permissionService = permissionService;
    this.roleService= roleService;
    this.mapper = mapper;
  }

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<PageResponse<Permission>>> listPermission(
      @Valid @RequestBody PaginationRequest request) {
    log.info("list permission");

    PageDto<PermissionDto> result = permissionService.paginate(new PaginationDto(request));

    List<Permission> permission = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<Permission> response = new PageResponse<>(result, permission);

    return ControllerUtil.createSuccessResponse(response, "success to fetch permission");
  }

  @GetMapping("/list")
  public ResponseEntity<ResponseModel<List<?>>> getPermissionByRole(@AuthenticationPrincipal AuthUser user){
    
    List<Permission> result = new ArrayList<>();
    Optional<String> userRole = user.getAuthorities().stream().map(m -> m.getAuthority()).findFirst();

    if(userRole.isPresent()){
      Optional<RoleDto> roleDto = roleService.findByName(userRole.get());

      if (roleDto.isPresent()){
        List<PermissionDto> permissionDto = permissionService.getPermissionByRole(roleDto.get().getCode());
        result = permissionDto.stream().map(mapper::dtoToHttp).toList();
      }
    }

    return ControllerUtil.createSuccessResponse(result, "success fetch permission");
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<Permission>> createPermission(
      @Validated(ValidationGroups.Create.class) @RequestBody Permission request) {
    log.info("create permission");

    PermissionDto dto = permissionService.create(mapper.httpToDto(request));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(dto), "success create permission");
  }

  @DeleteMapping("/delete/{permissionId}")
  public ResponseEntity<ResponseModel<String>> deletePermission(@PathVariable("permissionId") Integer permissionId) {
    log.info("delete permission");

    permissionService.delete(permissionId);

    return ControllerUtil.createSuccessResponse("Success", "success delete permission");
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseModel<Permission>> updatePermission(
      @Validated(ValidationGroups.Update.class) @RequestBody Permission permission) {
    log.info("update permission");
    
    PermissionDto dto = permissionService.update(mapper.httpToDto(permission));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(dto), "success update permission");
  }
}
