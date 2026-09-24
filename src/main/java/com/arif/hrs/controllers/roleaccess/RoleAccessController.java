package com.arif.hrs.controllers.roleaccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.RoleAccess;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.domain.service.RoleAccessServiceDomain;
import com.arif.hrs.mapper.RoleAccessMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role-access")
public class RoleAccessController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RoleAccessServiceDomain roleAccessService;
  private final RoleAccessMapper mapper;

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<?>> listRoleAccess(
      @RequestBody PaginationRequest request) {
    log.info("list role access");

    PageDto<RoleAccessDto> result = roleAccessService.paginate(new PaginationDto(request));

    List<RoleAccess> roleAccess = result.getData().stream()
        .map(mapper::dtoToHttp).toList();
    PageResponse<RoleAccess> response = new PageResponse<>(result, roleAccess);

    return ResponseEntity.ok(
        new ResponseModel<PageResponse<?>>(response, "success to fetch role access"));
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<RoleAccess>> createRoleAccess(
      @RequestBody RoleAccess request) {
    log.info("create role access");

    RoleAccessDto dto = roleAccessService.create(mapper.httpToDto(request));
    return ResponseEntity.ok(new ResponseModel<RoleAccess>(
        mapper.dtoToHttp(dto), "success to create role access"));
  }
}
