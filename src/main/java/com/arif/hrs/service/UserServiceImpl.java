package com.arif.hrs.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arif.hrs.domain.dto.AssignUserRoleDto;
import com.arif.hrs.domain.dto.FUserDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.mapper.UserMapper;
import com.arif.hrs.model.RoleModel;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.model.UserRoleModel;
import com.arif.hrs.repository.RoleRepository;
import com.arif.hrs.repository.UserRepository;
import com.arif.hrs.repository.UserRoleRepository;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilter;
import com.arif.hrs.service.serviceutility.CommonImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackOn = Exception.class)

@Slf4j
public class UserServiceImpl implements UserServiceDomain {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final RoleRepository roleRepository;
  private final UserMapper mapper;
  private final RoleMapper roleMapper;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
      RoleRepository roleRepository, UserMapper mapper, RoleMapper roleMapper,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
    this.roleRepository = roleRepository;
    this.mapper = mapper;
    this.roleMapper = roleMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<UserDto> getOne(Integer id) {
    log.info("Get User By Id {}", id);
    Optional<UserModel> model = userRepository.findById(id);
    UserDto dto = null;

    if (model.isPresent()) {
      dto = mapper.modelToDto(model.get());
    }
    return Optional.ofNullable(dto);
  }

  @Override
  public UserDto update(UserDto v) throws EntityNotFoundException {
    log.info("Update User");

    Optional<UserModel> model = userRepository.findById(v.getId());
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    UserModel xModel = model.get();
    xModel.setEmail(v.getEmail());
    xModel.setFullName(v.getFullName());
    xModel.setExpiryDate(v.getExpiryDate());

    if (v.getExpiryDate().before(Timestamp.from(Instant.now()))) {
      xModel.setActive(false);
    }

    xModel = userRepository.save(xModel);
    return mapper.modelToDto(xModel);
  }

  @Override
  public void delete(Integer id) throws EntityNotFoundException {
    log.info("Delete User");

    Optional<UserModel> model = userRepository.findById(id);
    if (!model.isPresent()) {
      throw new EntityNotFoundException();
    }

    userRepository.delete(model.get());
  }

  @Override
  public PageDto<UserDto> paginate(PaginationDto paginationRequest) {
    log.info("Paginate User");

    Specification<UserModel> specification = CommonImpl.buildFilterSpecificationDto(paginationRequest.getFilters(),
        UserDto.class);
    Pageable page = CommonImpl.buildPaginationPage(paginationRequest, "id", UserDto.class);

    Page<UserModel> pageModel = userRepository.findAll(specification, page);
    List<UserDto> dtoData = pageModel.getContent().stream().map(mapper::modelToDto).toList();

    return new PageDto<>(pageModel, dtoData);
  }

  @Override
  public UserDto createUser(FUserDto fUserDto) {
    UserModel model = new UserModel();
    model.setFullName(fUserDto.getFullName());
    model.setUserName(fUserDto.getUserName());
    model.setExpiryDate(Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMonths(3l).toInstant()));

    String encryptedPassword = passwordEncoder.encode(fUserDto.getPlainPassword());

    model.setPassword(encryptedPassword);

    model = userRepository.save(model);

    return mapper.modelToDto(model);
  }

  @Override
  public UserDto updatePassword(String userName, String oldPassword, String newPassword) {
    log.info("Update User Password {}, {}, {}", userName, oldPassword, newPassword);

    List<SpecificationFilter> filters = new ArrayList<>();

    filters
        .add(new SpecificationFilter("userName",
            userName, SpecificationEnum.EQ,
            SpecificationFilter.JOIN_FILTER_AND));

    SpecificationBuilder<UserModel> specificationBuilder = new SpecificationBuilder<UserModel>()
        .setFilter(filters);
    Optional<UserModel> user = userRepository.findOne(specificationBuilder.buildSpecification());

    if (!user.isPresent()) {
      throw new EntityNotFoundException();
    }

    UserModel xUser = user.get();

    if (!passwordEncoder.matches(oldPassword, xUser.getPassword())) {
      throw new ValidationException("Old Password is not matched");
    }

    final String encryptedNewPassword = passwordEncoder.encode(newPassword);
    xUser.setPassword(encryptedNewPassword);

    xUser = userRepository.save(xUser);

    return mapper.modelToDto(xUser);
  }

  @Override
  public List<RoleDto> getRolesByUserId(Integer userId) {
    log.info("Get Roles By User Id {}", userId);

    Optional<UserModel> model = userRepository.findById(userId);
    if (model.isEmpty()) {
      throw new EntityNotFoundException();
    }

    return model.get().getUserRoles().stream()
        .map(UserRoleModel::getRole)
        .filter(role -> role != null)
        .map(roleMapper::modelToDto)
        .toList();
  }

  @Override
  public void assignUserRole(AssignUserRoleDto assignUserRoleDto) {
    log.info("Assign User Role for user {}", assignUserRoleDto.getUserId());

    UserModel user = userRepository.findById(assignUserRoleDto.getUserId())
        .orElseThrow(EntityNotFoundException::new);

    Set<Integer> requestedRoleIds = assignUserRoleDto.getRoleId() == null
        ? new HashSet<>()
        : new HashSet<>(assignUserRoleDto.getRoleId());

    List<UserRoleModel> existingUserRoles = userRoleRepository.findByUserId(user.getId());

    List<UserRoleModel> userRolesToRemove = existingUserRoles.stream()
        .filter(userRole -> userRole.getRole() == null
            || !requestedRoleIds.contains(userRole.getRole().getId()))
        .toList();

    if (!userRolesToRemove.isEmpty()) {
      userRoleRepository.deleteAll(userRolesToRemove);
    }

    Set<Integer> existingRoleIds = existingUserRoles.stream()
        .map(UserRoleModel::getRole)
        .filter(Objects::nonNull)
        .map(RoleModel::getId)
        .collect(Collectors.toSet());

    List<Integer> roleIdsToAdd = requestedRoleIds.stream()
        .filter(roleId -> !existingRoleIds.contains(roleId))
        .toList();

    if (!roleIdsToAdd.isEmpty()) {
      List<UserRoleModel> userRolesToAdd = roleRepository.findAllById(roleIdsToAdd).stream()
          .map(role -> {
            UserRoleModel userRole = new UserRoleModel();
            userRole.setUser(user);
            userRole.setRole(role);
            return userRole;
          })
          .toList();

      userRoleRepository.saveAll(userRolesToAdd);
    }
  }

}
