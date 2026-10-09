package com.arif.hrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import com.arif.hrs.domain.dto.ShiftDto;
import com.arif.hrs.mapper.ShiftMapper;
import com.arif.hrs.model.ShiftModel;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.repository.ShiftRepository;
import com.arif.hrs.repository.ShiftToleranceRepository;
import com.arif.hrs.repository.ShiftTypeRepository;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

  @Mock
  private ShiftRepository shiftRepository;

  @Mock
  private ShiftTypeRepository shiftTypeRepository;

  @Mock
  private ShiftToleranceRepository shiftToleranceRepository;

  @Mock
  private ShiftMapper mapper;

  @InjectMocks
  private ShiftServiceImpl shiftService;

  @Test
  void getOneReturnsMappedShiftWhenFound() {
    ShiftModel model = shiftModel(1);
    ShiftDto dto = shiftDto(1, 1, 2, 3);
    when(shiftRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<ShiftDto> result = shiftService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(shiftRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsEmptyWhenShiftDoesNotExist() {
    when(shiftRepository.findById(1)).thenReturn(Optional.empty());

    Optional<ShiftDto> result = shiftService.getOne(1);

    assertTrue(result.isEmpty());
    verify(shiftRepository).findById(1);
  }

  @Test
  void createMapsAndSavesShift() {
    ShiftDto request = shiftDto(null, 1, 2, 3);
    ShiftModel model = shiftModel(null);
    ShiftModel savedModel = shiftModel(1);
    ShiftDto savedDto = shiftDto(1, 1, 2, 3);
    when(mapper.dtoToModel(request)).thenReturn(model);
    when(shiftRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    ShiftDto result = shiftService.create(request);

    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(shiftRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateChangesExistingShiftAndSavesRelatedModels() {
    Timestamp inTime = Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 8, 0));
    Timestamp outTime = Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 17, 0));
    ShiftDto request = shiftDto(1, 4, 2, 3);
    request.setInTime(inTime);
    request.setOutTime(outTime);
    ShiftModel existing = shiftModel(1);
    ShiftTypeModel shiftType = new ShiftTypeModel();
    ShiftToleranceModel inTolerance = new ShiftToleranceModel();
    ShiftToleranceModel outTolerance = new ShiftToleranceModel();
    ShiftDto updatedDto = shiftDto(1, 1, 2, 3);
    when(shiftRepository.findById(1)).thenReturn(Optional.of(existing));
    when(shiftTypeRepository.findById(4)).thenReturn(Optional.of(shiftType));
    when(shiftToleranceRepository.findById(2)).thenReturn(Optional.of(inTolerance));
    when(shiftToleranceRepository.findById(3)).thenReturn(Optional.of(outTolerance));
    when(shiftRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    ShiftDto result = shiftService.update(request);

    assertEquals(updatedDto, result);
    assertEquals(inTime, existing.getInTime());
    assertEquals(outTime, existing.getOutTime());
    assertEquals(shiftType, existing.getShiftType());
    assertEquals(inTolerance, existing.getInTolerance());
    assertEquals(outTolerance, existing.getOutTolerance());
    verify(shiftRepository).findById(1);
    verify(shiftTypeRepository).findById(4);
    verify(shiftToleranceRepository).findById(2);
    verify(shiftToleranceRepository).findById(3);
    verify(shiftRepository).save(existing);
    verify(mapper).modelToDto(existing);
  }

  @Test
  void updateThrowsWhenShiftDoesNotExist() {
    ShiftDto request = shiftDto(1, 4, null, null);
    when(shiftRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftService.update(request));

    verify(shiftRepository).findById(1);
  }

  @Test
  void updateThrowsWhenShiftTypeDoesNotExist() {
    ShiftDto request = shiftDto(1, 4, null, null);
    when(shiftRepository.findById(1)).thenReturn(Optional.of(shiftModel(1)));
    when(shiftTypeRepository.findById(4)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftService.update(request));

    verify(shiftRepository).findById(1);
    verify(shiftTypeRepository).findById(4);
  }

  @Test
  void deleteRemovesExistingShift() {
    ShiftModel model = shiftModel(1);
    when(shiftRepository.findById(1)).thenReturn(Optional.of(model));

    shiftService.delete(1);

    verify(shiftRepository).findById(1);
    verify(shiftRepository).delete(model);
  }

  @Test
  void deleteThrowsWhenShiftDoesNotExist() {
    when(shiftRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftService.delete(1));

    verify(shiftRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndUsesRequestedPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("shiftName");
    request.setSortDirection("asc");
    ShiftModel model = shiftModel(1);
    ShiftDto dto = shiftDto(1, 1, 2, 3);
    when(shiftRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftModel>>isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(model), PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<ShiftDto> result = shiftService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(shiftRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftModel>>isNull(), pageableCaptor.capture());
    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("shiftName", pageable.getSort().getOrderFor("shiftName").getProperty());
  }

  private static ShiftModel shiftModel(Integer id) {
    ShiftModel model = new ShiftModel();
    model.setId(id);
    model.setShiftName("Day shift");
    model.setInTime(Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 9, 0)));
    model.setOutTime(Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 18, 0)));
    return model;
  }

  private static ShiftDto shiftDto(
      Integer id,
      Integer shiftTypeId,
      Integer inToleranceId,
      Integer outToleranceId) {
    ShiftDto dto = new ShiftDto();
    dto.setId(id);
    dto.setCode("DAY");
    dto.setShiftName("Day shift");
    dto.setShiftTypeId(shiftTypeId);
    dto.setInToleranceId(inToleranceId);
    dto.setOutToleranceId(outToleranceId);
    return dto;
  }
}
