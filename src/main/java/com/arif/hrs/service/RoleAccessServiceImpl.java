package com.arif.hrs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.domain.service.RoleAccessServiceDomain;
import com.arif.hrs.mapper.RoleAccessMapper;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.repository.RoleAccessRepository;
import com.arif.hrs.repository.specification.SpecificationFilter;
import com.arif.hrs.repository.specification.specifications.RoleAccessSpecification;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class RoleAccessServiceImpl implements RoleAccessServiceDomain {

  private final RoleAccessRepository roleAccessRepository;
  private final RoleAccessMapper mapper;

  @Override
  public Optional<RoleAccessDto> getOne(Integer id) {

    Optional<RoleAccessModel> model = roleAccessRepository.findById(id);

    RoleAccessDto dto = null;

    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }

    return Optional.ofNullable(dto);
  }

  @Override
  public RoleAccessDto create(RoleAccessDto v) {
    RoleAccessModel model = mapper.dtoToModel(v);
    model = roleAccessRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public RoleAccessDto update(RoleAccessDto v) throws EntityNotFoundException {
    Optional<RoleAccessModel> model = roleAccessRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    RoleAccessModel xModel = model.get();
    AccessModel accessModel = new AccessModel();
    accessModel.setId(v.getAccessId());
    RoleModel roleModel = new RoleModel();
    roleModel.setId(v.getRoleId());

    xModel.setAccess(accessModel);
    xModel.setRole(roleModel);

    xModel = roleAccessRepository.save(xModel);

    return mapper.modelToDto(xModel);
  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    Optional<RoleAccessModel> model = roleAccessRepository.findById(id);
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }
    roleAccessRepository.delete(model.get());
  }

  @Override
  public PageDto<RoleAccessDto> paginate(PaginationDto paginationRequest) {

    List<SpecificationFilter> filters = paginationRequest.getFilters().stream()
        .map(m -> m.toSpecification())
        .toList();

    for (SpecificationFilter f : filters) {
      if (f.getFieldName().equalsIgnoreCase("accessName")) {
        f.setJoinTable("access");
        f.setJoinField("name");
        f.setJoinType("LEFT");
      }

      if (f.getFieldName().equalsIgnoreCase("roleName")) {
        f.setJoinTable("role");
        f.setJoinField("name");
        f.setJoinType("LEFT");
      }
    }
    Specification<RoleAccessModel> specification = CommonImpl.buildFilterSpecification(filters);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id");

    Page<RoleAccessModel> pageModel = roleAccessRepository.findAll(specification, page);
    List<RoleAccessDto> dtoResult = pageModel.getContent()
        .stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, dtoResult);

  }

  @Override
  public List<RoleAccessDto> findByAccessId(Integer accessId) {
    Specification<RoleAccessModel> specification = RoleAccessSpecification.filterRoleAccessByAccessId(accessId);
    List<RoleAccessModel> roleAccessModels = CommonImpl.filterBySpecification(specification, roleAccessRepository);

    return roleAccessModels.stream().map(mapper::modelToDto).toList();
  }

}
