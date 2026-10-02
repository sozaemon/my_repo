package com.arif.hrs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftDto;
import com.arif.hrs.domain.service.ShiftServiceDomain;
import com.arif.hrs.mapper.ShiftMapper;
import com.arif.hrs.model.ShiftModel;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.repository.ShiftRepository;
import com.arif.hrs.repository.ShiftToleranceRepository;
import com.arif.hrs.repository.ShiftTypeRepository;
import com.arif.hrs.repository.specification.SpecificationFilter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class ShiftServiceImpl implements ShiftServiceDomain {

  private final ShiftRepository shiftRepository;
  private final ShiftTypeRepository shiftTypeRepository;
  private final ShiftToleranceRepository shiftToleranceRepository;
  private final ShiftMapper mapper;

  @Override
  public Optional<ShiftDto> getOne(Integer id) {
    log.info("Get Shift By Id {}", id);

    Optional<ShiftModel> shiftModel = shiftRepository.findById(id);

    ShiftDto dto = null;

    if (shiftModel.isPresent()) {
      dto = mapper.modelToDto(shiftModel.get());
    }
    return Optional.ofNullable(dto);
  }

  @Override
  public ShiftDto create(ShiftDto v) {
    log.info("Create Shift {}", v.toString());

    ShiftModel model = mapper.dtoToModel(v);
    model = shiftRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public ShiftDto update(ShiftDto v) throws EntityNotFoundException {
    log.info("Update Shift {}", v.toString());

    Optional<ShiftModel> shiftModel = shiftRepository.findById(v.getId());
    if (!shiftModel.isPresent()) {
      log.error("cannot find shift with id : {}", v.getId());
      throw new EntityNotFoundException("cannot find shift");
    }
    Optional<ShiftTypeModel> shiftTypeModel = shiftTypeRepository.findById(v.getId());
    if (!shiftTypeModel.isPresent()) {
      log.error("cannot find shift type with id : {}", v.getShiftTypeId());
      throw new EntityNotFoundException("cannot find shift type");
    }

    ShiftModel shift = shiftModel.get();
    shift.setInTime(v.getInTime());
    shift.setOutTime(v.getOutTime());
    shift.setShiftType(shiftTypeModel.get());

    if (v.getInToleranceId() != null) {
      Optional<ShiftToleranceModel> inTolerance = shiftToleranceRepository.findById(v.getInToleranceId());
      if (inTolerance.isPresent()) {
        shift.setInTolerance(inTolerance.get());
      }
    }

    if (v.getOutToleranceId() != null) {
      Optional<ShiftToleranceModel> outTolerance = shiftToleranceRepository.findById(v.getOutToleranceId());
      if (outTolerance.isPresent()) {
        shift.setOutTolerance(outTolerance.get());
      }
    }

    shift = shiftRepository.save(shift);

    return mapper.modelToDto(shift);

  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    log.info("Delete Shift {}", id);

    Optional<ShiftModel> shiftModel = shiftRepository.findById(id);
    if (!shiftModel.isPresent()) {
      log.error("cannot find shift with id : {}", id);
      throw new EntityNotFoundException("cannot find shift");
    }

    shiftRepository.delete(shiftModel.get());
  }

  @Override
  public PageDto<ShiftDto> paginate(PaginationDto paginationRequest) {
    log.info("List Paginated Shift");

    List<SpecificationFilter> filters = paginationRequest.getFilters().stream()
        .map(m -> m.toSpecification()).toList();

    for (SpecificationFilter f : filters) {

      switch (f.getFieldName()) {
        case "shiftTypeName":
          f.setJoinTable("shiftType");
          f.setJoinField("name");
          f.setJoinType("LEFT");
          break;
        case "inToleranceName":
          f.setJoinTable("inTolerance");
          f.setJoinField("toleranceName");
          f.setJoinType("LEFT");
          break;
        case "outToleranceName":
          f.setJoinTable("outTolerance");
          f.setJoinField("toleranceName");
          f.setJoinType("LEFT");
          break;
        default:
          break;
      }
    }

    Specification<ShiftModel> specification = CommonImpl.buildFilterSpecification(filters);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id");

    Page<ShiftModel> pageModel = shiftRepository.findAll(specification, page);
    List<ShiftDto> dtoResult = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, dtoResult);
  }

}
