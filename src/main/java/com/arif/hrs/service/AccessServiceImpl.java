package com.arif.hrs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.service.AccessServiceDomain;
import com.arif.hrs.mapper.AccessMapper;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.repository.AccessRepository;
import com.arif.hrs.repository.specification.specifications.AccessSpecification;
import com.arif.hrs.service.serviceutility.CommonImpl;
import com.arif.hrs.util.excel.ExcelGenerator;
import com.arif.hrs.util.excel.excelmodel.AccessExcelModel;
import com.arif.hrs.util.excel.service.ExcelBuilderXlsxService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class AccessServiceImpl implements AccessServiceDomain {

  private final AccessRepository accessRepository;
  private final AccessMapper mapper;

  public AccessServiceImpl(AccessRepository accessRepository, AccessMapper mapper) {
    this.accessRepository = accessRepository;
    this.mapper = mapper;
  }

  @Override
  public List<AccessDto> getAccessByRolesAndPathAndMethod(List<String> roleName, String method) {
    log.info("get access by roles method: {}, roleNames : {}", method, roleName);

    Specification<AccessModel> specification = AccessSpecification.filterByRoleNameAndMethod(roleName, method);
    List<AccessModel> access = CommonImpl.filterBySpecification(specification, accessRepository);
    return access.stream().map(mapper::modelToDto).toList();
  }

  @Override
  public Optional<AccessDto> getOne(Integer id) {
    AccessDto result = null;

    Optional<AccessModel> model = accessRepository.findById(id);

    if (model.isPresent()) {
      result = mapper.modelToDto(model.get());
    }

    return Optional.ofNullable(result);
  }

  @Override
  public AccessDto create(AccessDto v) {
    AccessModel model = mapper.dtoToModel(v);

    model = accessRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public AccessDto update(AccessDto v) throws EntityNotFoundException {
    Optional<AccessModel> model = accessRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    AccessModel rModel = model.get();
    rModel.setMethod(v.getMethod());
    rModel.setName(v.getName());
    rModel.setPath(v.getPath());
    rModel.setDescription(v.getDescription());

    rModel = accessRepository.save(rModel);

    return mapper.modelToDto(rModel);

  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    Optional<AccessModel> model = accessRepository.findById(id);

    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    accessRepository.delete(model.get());
  }

  @Override
  public PageDto<AccessDto> paginate(PaginationDto paginationRequest) {

    Specification<AccessModel> specification = CommonImpl.buildFilterSpecificationDto(paginationRequest.getFilters(),
        AccessDto.class);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id", AccessDto.class);

    Page<AccessModel> pageModel = accessRepository.findAll(specification, page);
    List<AccessDto> dtoResult = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, dtoResult);
  }

  @Override
  public ByteArrayInputStream exportToExcel() {
    List<AccessModel> access = accessRepository.findAll();
    List<AccessExcelModel> excelData = access.stream().map(mapper::modelToExcel).toList();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Workbook wb = null;
    try {
      wb = ExcelGenerator.createExcel(new ExcelBuilderXlsxService<AccessExcelModel>(excelData, AccessExcelModel.class));
      wb.write(out);
    } catch (IOException e) {
      log.error("fail to generate write to byte", e);
    } finally {
      try {
        if (wb != null) {
          wb.close();
        }
      } catch (IOException e) {
        log.error("fail to close workbook", e);
      }
    }

    return new ByteArrayInputStream(out.toByteArray());
  }
}
