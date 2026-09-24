package com.arif.hrs.controllers.access;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.Access;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.service.AccessServiceDomain;
import com.arif.hrs.mapper.AccessMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessController {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AccessServiceDomain accessService;
  private final AccessMapper mapper;

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<?>> listAccess(@RequestBody PaginationRequest request) {
    log.info("list access");

    PageDto<AccessDto> result = accessService.paginate(new PaginationDto(request));

    List<Access> access = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<Access> response = new PageResponse<>(result, access);

    return ResponseEntity.ok(new ResponseModel<PageResponse<?>>(response, "success to fetch data"));
  }

  @GetMapping("/excel")
  public ResponseEntity<InputStreamResource> exportToExcel() {
    log.info("export access to excel");

    ByteArrayInputStream is = accessService.exportToExcel();

    HttpHeaders headers = new HttpHeaders();

    headers.add("Content-Disposition", "attachment; filename=users_report.xlsx");

    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(new InputStreamResource(is));
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<Access>> createAccess(@RequestBody Access request) {
    log.info("create access");

    AccessDto resultDto = accessService.create(mapper.httpToDto(request));
    return ResponseEntity.ok(new ResponseModel<Access>(mapper.dtoToHttp(resultDto), "success to create access"));
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseModel<Access>> updateAccess(@RequestBody Access request) {
    log.info("update access");

    AccessDto resultDto = accessService.update(mapper.httpToDto(request));
    return ResponseEntity.ok(new ResponseModel<Access>(mapper.dtoToHttp(resultDto), "success to update access"));
  }

  @DeleteMapping("/delete/{accessId}")
  public ResponseEntity<ResponseModel<String>> deleteAccess(@PathVariable Integer accessId) {
    log.info("delete access");

    accessService.delete(accessId);
    return ResponseEntity.ok(new ResponseModel<String>(null, "success to delete access"));
  }

}
