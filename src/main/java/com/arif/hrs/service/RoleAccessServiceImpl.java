package com.arif.hrs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.domain.dto.RoleAccessUploadDto;
import com.arif.hrs.domain.dto.UploadHeaderDto;
import com.arif.hrs.domain.service.RoleAccessServiceDomain;
import com.arif.hrs.mapper.RoleAccessMapper;
import com.arif.hrs.mapper.RoleAccessUploadMapper;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.mapper.UploadHeaderMapper;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleAccessUploadModel;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.model.UploadHeaderModel;
import com.arif.hrs.model.UploadProperty;
import com.arif.hrs.model.enums.AccessMethodEnum;
import com.arif.hrs.model.enums.UploadHeaderStatusEnum;
import com.arif.hrs.model.enums.UploadStatusEnum;
import com.arif.hrs.repository.AccessRepository;
import com.arif.hrs.repository.RoleAccessRepository;
import com.arif.hrs.repository.RoleAccessUploadRepository;
import com.arif.hrs.repository.RoleRepository;
import com.arif.hrs.repository.UploadHeaderRepository;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilterWithJoin;
import com.arif.hrs.repository.specification.specifications.RoleAccessSpecification;
import com.arif.hrs.service.serviceutility.CommonImpl;
import com.arif.hrs.util.excel.ExcelGenerator;
import com.arif.hrs.util.excel.ExcelReader;
import com.arif.hrs.util.excel.excelmodel.RoleExcelModel;
import com.arif.hrs.util.excel.service.RoleAccessTemplateExcelBuilder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class RoleAccessServiceImpl implements RoleAccessServiceDomain {

  private final RoleAccessRepository roleAccessRepository;
  private final RoleAccessMapper mapper;
  private final RoleRepository roleRepository;
  private final UploadHeaderRepository uploadHeaderRepository;
  private final RoleAccessUploadRepository roleAccessUploadRepository;
  private final UploadHeaderMapper uploadHeaderMapper;
  private final RoleAccessUploadMapper uploadMapper;
  private final AccessRepository accessRepository;
  private final RoleMapper roleMapper;

  public RoleAccessServiceImpl(RoleAccessRepository roleAccessRepository, RoleAccessMapper mapper,
      RoleRepository roleRepository, UploadHeaderRepository uploadHeaderRepository,
      RoleAccessUploadRepository roleAccessUploadRepository,
      UploadHeaderMapper uploadHeaderMapper,
      RoleAccessUploadMapper uploadMapper,
      AccessRepository accessRepository,
      RoleMapper roleMapper) {
    this.roleAccessRepository = roleAccessRepository;
    this.mapper = mapper;
    this.roleRepository = roleRepository;
    this.uploadHeaderRepository = uploadHeaderRepository;
    this.roleAccessUploadRepository = roleAccessUploadRepository;
    this.uploadHeaderMapper = uploadHeaderMapper;
    this.uploadMapper = uploadMapper;
    this.accessRepository = accessRepository;
    this.roleMapper = roleMapper;
  }

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
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id", RoleAccessDto.class);

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

  @Override
  public UploadHeaderDto uploadRoleAccess(MultipartFile file, String description) {
    final String[] uploadHeader = new String[] {
        "Name",
        "Path",
        "Method",
        "Description",
        "Roles" };
    List<RoleModel> roles = roleRepository.findAll();

    UploadHeaderModel header = new UploadHeaderModel();
    header.setModuleName("Role Access");
    header.setDescription(description);
    header.setStatus(UploadHeaderStatusEnum.UPLOADED);

    final UploadHeaderModel savedHeader = uploadHeaderRepository.save(header);

    ExcelReader<RoleAccessUploadModel> excelReader = new ExcelReader<RoleAccessUploadModel>() {
      @Override
      protected RoleAccessUploadModel mapToResult(Map<String, Object> input) {

        UploadStatusEnum status = UploadStatusEnum.VALID;
        String[] sRole = String.valueOf(input.get("Roles")).split(",");
        List<String> message = new ArrayList<>();

        for (String r : sRole) {
          String roleCode = r.trim();
          boolean valid = roles.stream().anyMatch(a -> a.getCode().equals(roleCode));
          if (!valid) {
            status = UploadStatusEnum.NOTVALID;
            message.add(String.format("Invalid Role: %s", roleCode));
          }
        }

        String inputMethod = String.valueOf(input.get("Method"));
        if (!Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE").contains(inputMethod)) {
          message.add(String.format("Invalid Method: %s", inputMethod));
          status = UploadStatusEnum.NOTVALID;
        }

        RoleAccessUploadModel result = new RoleAccessUploadModel();

        result.setUploadHeader(savedHeader);

        result.setName(String.valueOf(input.get("Name")));
        result.setPath(String.valueOf(input.get("Path")));
        result.setMethod(AccessMethodEnum.valueOf(String.valueOf(input.get("Method"))));
        result.setDescription(String.valueOf(input.get("Description")));
        result.setRoles(String.valueOf(input.get("Roles")));

        UploadProperty prop = new UploadProperty();
        prop.setUploadStatus(status);

        if (!message.isEmpty()) {
          prop.setErrorMessage(Strings.join(message, ','));
        }

        result.setUploadProperty(prop);
        return result;
      }

    };

    try {
      List<RoleAccessUploadModel> models = excelReader.parseWorkbook(file, uploadHeader, 2, 0);

      List<RoleAccessUploadModel> savedUploadData = new ArrayList<>();
      for (RoleAccessUploadModel m : models) {

        boolean alreadyInserted = savedUploadData.stream()
            .anyMatch(f -> (f.getPath().equals(m.getPath()) && f.getMethod().equals(m.getMethod()))
                || f.getName().equals(m.getName()));

        if (alreadyInserted) {
          m.getUploadProperty().setUploadStatus(UploadStatusEnum.NOTVALID);
          m.getUploadProperty().setErrorMessage("access data already exists in data set");
        }
        roleAccessUploadRepository.save(m);
      }

      // validate path and method
      roleAccessUploadRepository.validateUploadAccessPathAndMethod(header.getId());
    } catch (Exception e) {
      log.error("Upload fail", e);
    }

    return uploadHeaderMapper.modelToDto(header);
  }

  @Override
  public ByteArrayInputStream generateUploadTemplate() {
    List<RoleExcelModel> roles = roleRepository.findAll().stream()
        .map(roleMapper::modelToExcel).toList();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Workbook wb = null;
    try {
      wb = ExcelGenerator.createExcel(new RoleAccessTemplateExcelBuilder(roles));
      wb.write(out);
    } catch (IOException e) {
      log.error("fail to generate role access upload template", e);
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

  @Override
  public PageDto<RoleAccessUploadDto> pageUpload(PaginationDto pageRequest) {
    Specification<RoleAccessUploadModel> specification = CommonImpl
        .buildFilterSpecificationDto(pageRequest.getFilters(), RoleAccessUploadDto.class);
    Pageable page = CommonImpl.buildPaginationPage(pageRequest, "id", RoleAccessUploadDto.class);
    Page<RoleAccessUploadModel> pageModel = roleAccessUploadRepository.findAll(specification, page);

    List<RoleAccessUploadDto> dto = pageModel.getContent().stream()
        .map(uploadMapper::modelToDto).toList();

    return new PageDto<>(pageModel, dto);

  }

  @Override
  public RoleAccessUploadDto updateUpload(RoleAccessUploadDto uploadData) {

    if (uploadData.getId() == null) {
      throw new IllegalArgumentException("upload ID is required");
    }

    Optional<RoleAccessUploadModel> model = roleAccessUploadRepository.findById(uploadData.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    RoleAccessUploadModel m = model.get();

    List<SpecificationFilterWithJoin> filters = Arrays.asList(
        new SpecificationFilterWithJoin(
            "path",
            uploadData.getPath(),
            SpecificationEnum.EQ,
            "AND"),
        new SpecificationFilterWithJoin(
            "method",
            uploadData.getMethod(),
            SpecificationEnum.EQ,
            "AND"));

    Specification<AccessModel> accessSpecification = new SpecificationBuilder<AccessModel>(filters)
        .buildSpecification();

    Optional<AccessModel> existingAccess = this.accessRepository.findOne(accessSpecification);
    if (existingAccess.isPresent()) {
      throw new IllegalArgumentException("Access already exists");
    }

    String[] roles = uploadData.getRoles().split(",");

    if (roles.length > 0) {
      List<RoleModel> exRole = roleRepository.findAll();
      List<String> roleCode = exRole.stream().map(role -> role.getCode()).toList();

      for (String r : roles) {
        if (!roleCode.contains(r)) {
          throw new IllegalArgumentException(String.format("Invalid role code %s", r));
        }
      }
    }

    m.setName(uploadData.getName());
    m.setPath(uploadData.getPath());
    m.setMethod(uploadData.getMethod());
    m.setDescription(uploadData.getDescription());
    m.setRoles(uploadData.getRoles());

    UploadProperty prop = new UploadProperty();
    prop.setErrorMessage(null);
    prop.setUploadStatus(UploadStatusEnum.VALID);

    m.setUploadProperty(prop);

    m = roleAccessUploadRepository.save(m);

    return uploadMapper.modelToDto(m);
  }

  @Override
  public void deleteRoleAccessUpload(Integer uploadId) {
    Optional<RoleAccessUploadModel> model = roleAccessUploadRepository.findById(uploadId);

    if (!model.isPresent()) {
      throw new EntityNotFoundException("upload data not found");
    }

    roleAccessUploadRepository.delete(model.get());
  }

  @Override
  public void processUploadData(Integer uploadHeaderId) {

    Optional<UploadHeaderModel> header = uploadHeaderRepository.findById(uploadHeaderId);

    if (header.isEmpty()) {
      throw new EntityNotFoundException("invalid header id");
    }

    UploadHeaderModel headerModel = header.get();

    if (headerModel.getStatus().equals(UploadHeaderStatusEnum.COMPLETE)) {
      throw new IllegalArgumentException("Header already processed and complete");
    }

    List<RoleAccessUploadModel> uploads = roleAccessUploadRepository
        .findAll(RoleAccessSpecification.findUploadDataByHeaderId(uploadHeaderId));

    List<RoleModel> roles = roleRepository.findAll();

    for (RoleAccessUploadModel m : uploads) {
      // create access
      // each role create role access data
      AccessModel access = new AccessModel();
      access.setName(m.getName());
      access.setPath(m.getPath());
      access.setMethod(m.getMethod());
      access.setDescription(m.getDescription());

      access = accessRepository.save(access);

      String[] roleCodes = m.getRoles().split(",");

      for (String rCode : roleCodes) {
        Optional<RoleModel> role = roles.stream().filter(f -> f.getCode().equals(rCode)).findFirst();

        if (role.isPresent()) {
          RoleAccessModel roleAccess = new RoleAccessModel();
          roleAccess.setAccess(access);
          roleAccess.setRole(role.get());

          roleAccessRepository.save(roleAccess);
        }
      }

      m.getUploadProperty().setUploadStatus(UploadStatusEnum.COMPLETED);
      roleAccessUploadRepository.save(m);

    }
    headerModel.setStatus(UploadHeaderStatusEnum.COMPLETE);
    uploadHeaderRepository.save(headerModel);
  }

}
