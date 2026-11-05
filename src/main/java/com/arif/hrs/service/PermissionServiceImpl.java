package com.arif.hrs.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.PermissionDto;
import com.arif.hrs.domain.service.PermissionServiceDomain;
import com.arif.hrs.mapper.PermissionMapper;
import com.arif.hrs.model.PermissionModel;
import com.arif.hrs.repository.PermissionRepository;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilterWithJoin;
import com.arif.hrs.service.serviceutility.CommonImpl;
import com.arif.hrs.service.serviceutility.FilterValidator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service("permissionService")
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class PermissionServiceImpl implements PermissionServiceDomain {

  private final PermissionRepository permissionRepository;
  private final PermissionMapper mapper;

  public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper mapper) {
    this.permissionRepository = permissionRepository;
    this.mapper = mapper;
  }

  @Override
  public Optional<PermissionDto> getOne(Integer id) {
    log.info("get permission by id {}", id);

    Optional<PermissionModel> model = permissionRepository.findById(id);

    PermissionDto dto = null;
    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }

    return Optional.ofNullable(dto);
  }

  @Override
  public PermissionDto create(PermissionDto v) {
    log.info("create permission");

    PermissionModel model = mapper.dtoToModel(v);

    model = permissionRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public PermissionDto update(PermissionDto v) throws EntityNotFoundException {
    log.info("update permission");

    Optional<PermissionModel> model = permissionRepository.findById(v.getId());

    if (!model.isPresent()) {
      throw new EntityNotFoundException("cannot find permission");
    }

    PermissionModel xModel = model.get();
    xModel.setPermission(v.getPermission());
    xModel.setRoleCode(v.getRoleCode());

    xModel = permissionRepository.save(xModel);
    return mapper.modelToDto(xModel);
  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    log.info("delete permission");
    Optional<PermissionModel> model = permissionRepository.findById(id);

    if (!model.isPresent()) {
      throw new EntityNotFoundException("cannot find permission");
    }

    permissionRepository.delete(model.get());
  }

  @Override
  public PageDto<PermissionDto> paginate(PaginationDto paginationRequest) {
    log.info("paginate permission");
    Specification<PermissionModel> specification = CommonImpl
        .buildFilterSpecificationDto(paginationRequest.getFilters(), PermissionDto.class);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id", PermissionDto.class);

    Page<PermissionModel> pageModel = permissionRepository.findAll(specification, page);
    List<PermissionDto> data = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, data);

  }

  @Override
  public List<PermissionDto> getPermissionByRole(String roleCode) {
    
    log.info("get permission by role");

    List<SpecificationFilterWithJoin> filters = Arrays.asList(
      new SpecificationFilterWithJoin("roleCode", roleCode, SpecificationEnum.EQ, "AND"));

    FilterValidator filterValidator = new FilterValidator(filters, PermissionDto.class);

    SpecificationBuilder<PermissionModel> builder = new SpecificationBuilder<>(filterValidator.buildAndValidateFilters());
    builder.setMapping(filterValidator.extractMappingProperty());

    List<PermissionModel> dataList = permissionRepository.findAll(builder.buildSpecification());

    return dataList.stream().map(mapper::modelToDto).toList();
  }

}
