package com.arif.hrs.controllers.shift;

import java.util.List;
import java.util.Optional;

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
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.Shift;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftDto;
import com.arif.hrs.domain.service.ShiftServiceDomain;
import com.arif.hrs.mapper.ShiftMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

  private final ShiftServiceDomain shiftService;
  private final ShiftMapper mapper;

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<?>> listShift(
      @RequestBody PaginationRequest request) {

    log.info("list shift");

    PageDto<ShiftDto> result = shiftService.paginate(new PaginationDto(request));

    List<Shift> shift = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<Shift> response = new PageResponse<>(result, shift);

    return ResponseEntity.ok(
        new ResponseModel<PageResponse<Shift>>(response, "success to fetch shift"));
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<Shift>> createShift(
      @RequestBody Shift request) {
    log.info("Create Shift");

    ShiftDto result = shiftService.create(mapper.httpToDto(request));
    return ResponseEntity.ok(new ResponseModel<Shift>(mapper.dtoToHttp(result), "Success create Shift"));
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseModel<Shift>> updateShift(
      @RequestBody Shift request) {
    log.info("Update Shift");

    ShiftDto result = shiftService.update(mapper.httpToDto(request));
    return ResponseEntity.ok(new ResponseModel<Shift>(mapper.dtoToHttp(result), "Success update Shift"));
  }

  @GetMapping("/{shiftId}")
  public ResponseEntity<ResponseModel<Shift>> getShift(@PathVariable("shiftId") Integer shiftId) {
    log.info("Get Shift {}", shiftId);

    Optional<ShiftDto> shift = shiftService.getOne(shiftId);

    Shift result = null;

    if (shift.isPresent()) {
      result = mapper.dtoToHttp(shift.get());
    }

    return ResponseEntity.ok(new ResponseModel<Shift>(result, "Success to fetch Shift"));
  }

  @DeleteMapping("/delete/{shiftId}")
  public ResponseEntity<ResponseModel<?>> deleteShift(@PathVariable("shiftId") Integer shiftId) {
    log.info("Delete Shift {}", shiftId);

    shiftService.delete(shiftId);
    return ResponseEntity.ok(new ResponseModel<String>("Shift Deleted", "Shift Deleted"));
  }
}
