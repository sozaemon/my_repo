package com.arif.hrs.controllers.shift;

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
import com.arif.hrs.controllers.httpmodel.ShiftType;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.domain.service.ShiftTypeServiceDomain;
import com.arif.hrs.mapper.ShiftTypeMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shift/shift-type")
@RequiredArgsConstructor
public class ShiftTypeController {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final ShiftTypeServiceDomain shiftTypeService;
  private final ShiftTypeMapper mapper;

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<ShiftType>> createShiftType(@RequestBody ShiftType shiftType) {
    log.info("create shift type");

    ShiftTypeDto dto = shiftTypeService.create(mapper.httpToDto(shiftType));
    return ResponseEntity.ok(new ResponseModel<ShiftType>(mapper.dtoToHttp(dto), "success to create shift type"));
  }

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<?>> listShiftType(@RequestBody PaginationRequest request) {
    log.info("list shift type");

    PageDto<ShiftTypeDto> result = shiftTypeService.paginate(new PaginationDto(request));

    List<ShiftType> shiftType = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<ShiftType> response = new PageResponse<>(result, shiftType);

    return ResponseEntity.ok(new ResponseModel<PageResponse<?>>(response, "success to fetch shift types"));
  }

}
