package com.arif.hrs.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.domain.service.ShiftToleranceServiceDomain;
import com.arif.hrs.mapper.ShiftToleranceMapper;
import com.arif.hrs.model.ShiftToleranceModel;
import com.arif.hrs.repository.ShiftToleranceRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ShiftToleranceServiceImpl implements ShiftToleranceServiceDomain {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final ShiftToleranceRepository shiftToleranceRepository;
  private final ShiftToleranceMapper mapper;

  @Override
  public Optional<ShiftToleranceDto> getOne(Integer id) {
    log.info("get shift tolerance by id");

    Optional<ShiftToleranceModel> model = shiftToleranceRepository.findById(id);
    ShiftToleranceDto dto = null;

    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }
    return Optional.ofNullable(dto);
  }

  @Override
  public ShiftToleranceDto create(ShiftToleranceDto v) {
    log.info("create shift tolerance");

    ShiftToleranceModel model = mapper.dtoToModel(v);
    model = shiftToleranceRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public ShiftToleranceDto update(ShiftToleranceDto v) throws EntityNotFoundException {
    log.info("update shift tolerance");
    Optional<ShiftToleranceModel> model = shiftToleranceRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    ShiftToleranceModel xModel = model.get();
    xModel.setToleranceAfter(v.getToleranceAfter());
    xModel.setToleranceAfterUnit(v.getToleranceAfterUnit());
    xModel.setToleranceBefore(v.getToleranceBefore());
    xModel.setToleranceBeforeUnit(v.getToleranceBeforeUnit());
    xModel.setToleranceName(v.getToleranceName());

    xModel = shiftToleranceRepository.save(xModel);

    return mapper.modelToDto(xModel);
  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    log.info("delete shift tolerance");
    Optional<ShiftToleranceModel> model = shiftToleranceRepository.findById(id);
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    shiftToleranceRepository.delete(model.get());
  }

  @Override
  public PageDto<ShiftToleranceDto> paginate(PaginationDto paginationRequest) {
    log.info("paginated list shift tolerance");

    Specification<ShiftToleranceModel> specification = CommonImpl.buildPaginationSpecification(paginationRequest);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id");

    Page<ShiftToleranceModel> pageModel = shiftToleranceRepository.findAll(specification, page);
    List<ShiftToleranceDto> data = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, data);

  }

}
