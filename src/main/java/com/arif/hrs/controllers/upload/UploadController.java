package com.arif.hrs.controllers.upload;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.UploadHeader;
import com.arif.hrs.controllers.util.ControllerUtil;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.UploadHeaderDto;
import com.arif.hrs.domain.service.UploadHeaderServiceDomain;
import com.arif.hrs.mapper.UploadHeaderMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/upload")
@Slf4j
public class UploadController {

  private final UploadHeaderServiceDomain uploadHeaderService;
  private final UploadHeaderMapper mapper;

  public UploadController(UploadHeaderServiceDomain uploadHeaderService, UploadHeaderMapper mapper) {
    this.uploadHeaderService = uploadHeaderService;
    this.mapper = mapper;
  }

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<PageResponse<UploadHeader>>> list(
      @RequestBody PaginationRequest request) {
    PageDto<UploadHeaderDto> result = uploadHeaderService.paginate(new PaginationDto(request));
    List<UploadHeader> uploadHeader = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<UploadHeader> response = new PageResponse<>(result, uploadHeader);

    return ControllerUtil.createSuccessResponse(response, "success fetch upload header");
  }
}
