package com.arif.hrs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleServiceDomain {

  private final RoleRepository roleRepository;
  private final RoleMapper mapper;

  @Override
  public Optional<RoleDto> getOne(Integer id) {
    Optional<RoleModel> model = roleRepository.findById(id);

    RoleDto dto = null;
    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }

    return Optional.ofNullable(dto);
  }

  @Override
  public RoleDto create(RoleDto v) {

    RoleModel model = mapper.dtoToModel(v);
    model = roleRepository.save(model);
    return mapper.modelToDto(model);
  }

  @Override
  public RoleDto update(RoleDto v) throws EntityNotFoundException {
    Optional<RoleModel> model = roleRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    RoleModel rModel = model.get();
    rModel.setName(v.getName());
    rModel.setDescription(v.getDescription());

    rModel = roleRepository.save(rModel);

    return mapper.modelToDto(rModel);
  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    Optional<RoleModel> model = roleRepository.findById(id);

    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    roleRepository.delete(model.get());
  }

  @Override
  public PageDto<RoleDto> paginate(PaginationDto paginationRequest) {
    Specification<RoleModel> specification = CommonImpl.buildPaginationSpecification(paginationRequest);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id");

    Page<RoleModel> pageModel = roleRepository.findAll(specification, page);
    List<RoleDto> data = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, data);
  }

}
