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
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.mapper.ShiftTypeMapper;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.repository.ShiftTypeRepository;

@ExtendWith(MockitoExtension.class)
class ShiftTypeServiceTest {

  @Mock
  private ShiftTypeRepository shiftTypeRepository;

  @Mock
  private ShiftTypeMapper mapper;

  @InjectMocks
  private ShiftTypeServiceImpl shiftTypeService;

  @Test
  void getOneReturnsMappedShiftTypeWhenFound() {
    ShiftTypeModel model = shiftTypeModel(1, "REGULAR", "Regular");
    ShiftTypeDto dto = shiftTypeDto(1, "REGULAR", "Regular");
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.of(model));
    when(mapper.modelToDto(model)).thenReturn(dto);

    Optional<ShiftTypeDto> result = shiftTypeService.getOne(1);

    assertEquals(Optional.of(dto), result);
    verify(shiftTypeRepository).findById(1);
    verify(mapper).modelToDto(model);
  }

  @Test
  void getOneReturnsEmptyWhenShiftTypeDoesNotExist() {
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.empty());

    Optional<ShiftTypeDto> result = shiftTypeService.getOne(1);

    assertTrue(result.isEmpty());
    verify(shiftTypeRepository).findById(1);
  }

  @Test
  void createMapsAndSavesShiftType() {
    ShiftTypeDto request = shiftTypeDto(null, "REGULAR", "Regular");
    ShiftTypeModel model = shiftTypeModel(null, "REGULAR", "Regular");
    ShiftTypeModel savedModel = shiftTypeModel(1, "REGULAR", "Regular");
    ShiftTypeDto savedDto = shiftTypeDto(1, "REGULAR", "Regular");
    when(mapper.dtoToModel(request)).thenReturn(model);
    when(shiftTypeRepository.save(model)).thenReturn(savedModel);
    when(mapper.modelToDto(savedModel)).thenReturn(savedDto);

    ShiftTypeDto result = shiftTypeService.create(request);

    assertEquals(savedDto, result);
    verify(mapper).dtoToModel(request);
    verify(shiftTypeRepository).save(model);
    verify(mapper).modelToDto(savedModel);
  }

  @Test
  void updateChangesAndSavesExistingShiftType() {
    ShiftTypeDto request = shiftTypeDto(1, "REGULAR", "Updated regular");
    ShiftTypeModel existing = shiftTypeModel(1, "REGULAR", "Regular");
    ShiftTypeDto updatedDto = shiftTypeDto(1, "REGULAR", "Updated regular");
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.of(existing));
    when(shiftTypeRepository.save(existing)).thenReturn(existing);
    when(mapper.modelToDto(existing)).thenReturn(updatedDto);

    ShiftTypeDto result = shiftTypeService.update(request);

    assertEquals(updatedDto, result);
    assertEquals("Updated regular", existing.getName());
    verify(shiftTypeRepository).findById(1);
    verify(shiftTypeRepository).save(existing);
    verify(mapper).modelToDto(existing);
  }

  @Test
  void updateThrowsWhenShiftTypeDoesNotExist() {
    ShiftTypeDto request = shiftTypeDto(1, "REGULAR", "Updated regular");
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftTypeService.update(request));

    verify(shiftTypeRepository).findById(1);
  }

  @Test
  void deleteRemovesExistingShiftType() {
    ShiftTypeModel model = shiftTypeModel(1, "REGULAR", "Regular");
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.of(model));

    shiftTypeService.delete(1);

    verify(shiftTypeRepository).findById(1);
    verify(shiftTypeRepository).delete(model);
  }

  @Test
  void deleteThrowsWhenShiftTypeDoesNotExist() {
    when(shiftTypeRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> shiftTypeService.delete(1));

    verify(shiftTypeRepository).findById(1);
  }

  @Test
  void paginateReturnsMappedPageAndUsesRequestedPageable() {
    PaginationDto request = new PaginationDto();
    request.setPage(2);
    request.setPageSize(5);
    request.setSortBy("name");
    request.setSortDirection("asc");
    ShiftTypeModel model = shiftTypeModel(1, "REGULAR", "Regular");
    ShiftTypeDto dto = shiftTypeDto(1, "REGULAR", "Regular");
    when(shiftTypeRepository.findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftTypeModel>>isNull(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(model), PageRequest.of(1, 5), 11));
    when(mapper.modelToDto(model)).thenReturn(dto);

    PageDto<ShiftTypeDto> result = shiftTypeService.paginate(request);

    assertEquals(List.of(dto), result.getData());
    assertEquals(5, result.getSize());
    assertEquals(1, result.getPage());
    assertEquals(11L, result.getTotalSize());
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(shiftTypeRepository).findAll(
        org.mockito.ArgumentMatchers.<Specification<ShiftTypeModel>>isNull(),
        pageableCaptor.capture());
    Pageable pageable = pageableCaptor.getValue();
    assertEquals(1, pageable.getPageNumber());
    assertEquals(5, pageable.getPageSize());
    assertEquals("name", pageable.getSort().getOrderFor("name").getProperty());
  }

  private static ShiftTypeModel shiftTypeModel(Integer id, String code, String name) {
    ShiftTypeModel model = new ShiftTypeModel();
    model.setId(id);
    model.setCode(code);
    model.setName(name);
    return model;
  }

  private static ShiftTypeDto shiftTypeDto(Integer id, String code, String name) {
    ShiftTypeDto dto = new ShiftTypeDto();
    dto.setId(id);
    dto.setCode(code);
    dto.setName(name);
    return dto;
  }
}
