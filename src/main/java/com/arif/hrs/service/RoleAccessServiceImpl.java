package com.arif.hrs.service;

import java.util.ArrayList;
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
import com.arif.hrs.repository.specification.specifications.RoleAccessSpecification;
import com.arif.hrs.service.serviceutility.CommonImpl;

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

    Specification<RoleAccessModel> specification = CommonImpl.buildFilterSpecificationDto(
        paginationRequest.getFilters(),
        RoleAccessDto.class);
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

  @Override
  public List<RoleAccessDto> assignRolesToAccess(List<Integer> roleIds, Integer accessId) {

    List<RoleAccessDto> createdRoles = new ArrayList<>();

    Specification<RoleAccessModel> specification = RoleAccessSpecification.filterRoleAccessByAccessId(accessId);

    List<RoleAccessModel> existingRoleAccess = roleAccessRepository.findAll(specification);

    // delete not match role access
    for (RoleAccessModel r : existingRoleAccess) {
      if (!roleIds.contains(r.getRole().getId())) {
        roleAccessRepository.delete(r);
      }
    }

    // create not existing Role Access
    List<Integer> currentRoleIds = existingRoleAccess.stream().map(m -> m.getRole().getId()).toList();
    for (Integer r : roleIds) {
      if (!currentRoleIds.contains(r)) {

        RoleAccessDto dto = new RoleAccessDto();
        dto.setRoleId(r);
        dto.setAccessId(accessId);

        RoleAccessModel createdRole = roleAccessRepository.save(mapper.dtoToModel(dto));
        createdRoles.add(mapper.modelToDto(createdRole));
      }
    }

    return createdRoles;
  }

}
