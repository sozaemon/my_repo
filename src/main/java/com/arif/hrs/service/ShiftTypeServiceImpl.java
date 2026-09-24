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
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.domain.service.ShiftTypeServiceDomain;
import com.arif.hrs.mapper.ShiftTypeMapper;
import com.arif.hrs.model.ShiftTypeModel;
import com.arif.hrs.repository.ShiftTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ShiftTypeServiceImpl implements ShiftTypeServiceDomain {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final ShiftTypeRepository shiftTypeRepository;
  private final ShiftTypeMapper mapper;

  @Override
  public Optional<ShiftTypeDto> getOne(Integer id) {
    log.info("shift type service get one {}", id);

    Optional<ShiftTypeModel> model = shiftTypeRepository.findById(id);

    ShiftTypeDto dto = null;
    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }

    return Optional.ofNullable(dto);
  }

  @Override
  public ShiftTypeDto create(ShiftTypeDto v) {
    log.info("create shift type");

    ShiftTypeModel model = mapper.dtoToModel(v);

    model = shiftTypeRepository.save(model);
    return mapper.modelToDto(model);
  }

  @Override
  public ShiftTypeDto update(ShiftTypeDto v) throws EntityNotFoundException {
    log.info("update shift type");

    Optional<ShiftTypeModel> model = shiftTypeRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    ShiftTypeModel xModel = model.get();
    xModel.setName(v.getName());

    xModel = shiftTypeRepository.save(xModel);
    return mapper.modelToDto(xModel);

  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    log.info("delete shift type");

    Optional<ShiftTypeModel> model = shiftTypeRepository.findById(id);
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }
    shiftTypeRepository.delete(model.get());
  }

  @Override
  public PageDto<ShiftTypeDto> paginate(PaginationDto paginationRequest) {
    log.info("delete shift type");

    Specification<ShiftTypeModel> specification = CommonImpl.buildPaginationSpecification(paginationRequest);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id");

    Page<ShiftTypeModel> pageModel = shiftTypeRepository.findAll(specification, page);
    List<ShiftTypeDto> data = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, data);

  }
}
