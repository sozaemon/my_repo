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
import com.arif.hrs.controllers.httpmodel.ShiftTolerance;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.domain.service.ShiftToleranceServiceDomain;
import com.arif.hrs.mapper.ShiftToleranceMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shift/shift-tolerance")
@RequiredArgsConstructor
public class ShiftToleranceController {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final ShiftToleranceServiceDomain shiftToleranceService;
  private final ShiftToleranceMapper mapper;

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<ShiftTolerance>> createShiftTolerance(@RequestBody ShiftTolerance request) {
    log.info("create shift tolerance");

    ShiftToleranceDto dto = shiftToleranceService.create(mapper.httpToDto(request));

    return ResponseEntity
        .ok(new ResponseModel<ShiftTolerance>(mapper.dtoToHttp(dto), "success to create shift tolerance"));
  }

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<?>> listShiftTolerance(@RequestBody PaginationRequest request) {
    log.info("list shift tolerance");

    PageDto<ShiftToleranceDto> result = shiftToleranceService.paginate(new PaginationDto(request));

    List<ShiftTolerance> shiftTolerance = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<ShiftTolerance> response = new PageResponse<>(result, shiftTolerance);

    return ResponseEntity.ok(new ResponseModel<PageResponse<?>>(response, "success to fetch shift tolerance"));

  }

}
