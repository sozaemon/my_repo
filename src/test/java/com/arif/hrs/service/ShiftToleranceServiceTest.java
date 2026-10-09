package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.mapper.ShiftToleranceMapper;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.model.enums.ToleranceUnitEnum;
import com.arif.hrs.repository.ShiftToleranceRepository;

@ExtendWith(MockitoExtension.class)
class ShiftToleranceServiceTest {

  @Mock
  private ShiftToleranceRepository shiftToleranceRepository;

  @Mock
  private ShiftToleranceMapper mapper;

  @InjectMocks
  private ShiftToleranceServiceImpl shiftToleranceService;

  @Test
  void getOneReturnsMappedShiftToleranceWhenFound() {
    ShiftToleranceModel model = shiftToleranceModel(1, "Flexible", 15, 20);
    ShiftToleranceDto dto = shiftToleranceDto(1, "Flexible", 15, 20);
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<ShiftToleranceDto> result = shiftToleranceService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(shiftToleranceRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsEmptyWhenShiftToleranceDoesNotExist() {
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.empty());

    Optional<ShiftToleranceDto> result = shiftToleranceService.getOne(1);

    assertTrue(result.isEmpty());
    verify(shiftToleranceRepository).findById(1);
  }

  @Test
  void createMapsAndSavesShiftTolerance() {
    ShiftToleranceDto request = shiftToleranceDto(null, "Flexible", 15, 20);
    ShiftToleranceModel model = shiftToleranceModel(null, "Flexible", 15, 20);
    ShiftToleranceModel savedModel = shiftToleranceModel(1, "Flexible", 15, 20);
    ShiftToleranceDto savedDto = shiftToleranceDto(1, "Flexible", 15, 20);
    when(mapper.dtoToModel(request)).thenReturn(model);
    when(shiftToleranceRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    ShiftToleranceDto result = shiftToleranceService.create(request);

    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(shiftToleranceRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateChangesAndSavesExistingShiftTolerance() {
    ShiftToleranceDto request = shiftToleranceDto(1, "Updated tolerance", 5, 10);
    request.setToleranceBeforeUnit(ToleranceUnitEnum.HOUR);
    request.setToleranceAfterUnit(ToleranceUnitEnum.MINUTE);
    ShiftToleranceModel existing = shiftToleranceModel(1, "Original tolerance", 15, 20);
    ShiftToleranceDto updatedDto = shiftToleranceDto(1, "Updated tolerance", 5, 10);
    updatedDto.setToleranceBeforeUnit(ToleranceUnitEnum.HOUR);
    updatedDto.setToleranceAfterUnit(ToleranceUnitEnum.MINUTE);
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.of(existing));
    when(shiftToleranceRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    ShiftToleranceDto result = shiftToleranceService.update(request);

    assertEquals(updatedDto, result);
    assertEquals("Updated tolerance", existing.getToleranceName());
    assertEquals(5, existing.getToleranceBefore());
    assertEquals(10, existing.getToleranceAfter());
    assertEquals(ToleranceUnitEnum.HOUR, existing.getToleranceBeforeUnit());
    assertEquals(ToleranceUnitEnum.MINUTE, existing.getToleranceAfterUnit());
    verify(shiftToleranceRepository).findById(1);
    verify(shiftToleranceRepository).save(existing);
    verify(mapper).modelToDto(existing);
  }

  @Test
  void updateThrowsWhenShiftToleranceDoesNotExist() {
    ShiftToleranceDto request = shiftToleranceDto(1, "Updated tolerance", 5, 10);
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftToleranceService.update(request));

    verify(shiftToleranceRepository).findById(1);
  }

  @Test
  void deleteRemovesExistingShiftTolerance() {
    ShiftToleranceModel model = shiftToleranceModel(1, "Flexible", 15, 20);
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.of(model));

    shiftToleranceService.delete(1);

    verify(shiftToleranceRepository).findById(1);
    verify(shiftToleranceRepository).delete(model);
  }

  @Test
  void deleteThrowsWhenShiftToleranceDoesNotExist() {
    when(shiftToleranceRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftToleranceService.delete(1));

    verify(shiftToleranceRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndUsesRequestedPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("toleranceName");
    request.setSortDirection("asc");
    ShiftToleranceModel model = shiftToleranceModel(1, "Flexible", 15, 20);
    ShiftToleranceDto dto = shiftToleranceDto(1, "Flexible", 15, 20);
    when(shiftToleranceRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftToleranceModel>>isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(model), PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<ShiftToleranceDto> result = shiftToleranceService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(shiftToleranceRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftToleranceModel>>isNull(),
        pageableCaptor.capture());
    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("toleranceName", pageable.getSort().getOrderFor("toleranceName").getProperty());
  }

  private static ShiftToleranceModel shiftToleranceModel(
      Integer id,
      String toleranceName,
      Integer toleranceBefore,
      Integer toleranceAfter) {
    ShiftToleranceModel model = new ShiftToleranceModel();
    model.setId(id);
    model.setCode("FLEX");
    model.setToleranceName(toleranceName);
    model.setToleranceBeforeUnit(ToleranceUnitEnum.MINUTE);
    model.setToleranceAfterUnit(ToleranceUnitEnum.MINUTE);
    model.setToleranceBefore(toleranceBefore);
    model.setToleranceAfter(toleranceAfter);
    return model;
  }

  private static ShiftToleranceDto shiftToleranceDto(
      Integer id,
      String toleranceName,
      Integer toleranceBefore,
      Integer toleranceAfter) {
    ShiftToleranceDto dto = new ShiftToleranceDto();
    dto.setId(id);
    dto.setCode("FLEX");
    dto.setToleranceName(toleranceName);
    dto.setToleranceBeforeUnit(ToleranceUnitEnum.MINUTE);
    dto.setToleranceAfterUnit(ToleranceUnitEnum.MINUTE);
    dto.setToleranceBefore(toleranceBefore);
    dto.setToleranceAfter(toleranceAfter);
    return dto;
  }
}
