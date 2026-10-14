package com.arif.hrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.UserAuthServiceDomain;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.mapper.UserMapper;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.repository.UserRepository;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilter;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class UserAuthServiceImpl implements UserAuthServiceDomain {

  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final RoleMapper roleMapper;

  public UserAuthServiceImpl(UserRepository userRepository, UserMapper mapper, RoleMapper roleMapper) {
    this.userRepository = userRepository;
    this.mapper = mapper;
    this.roleMapper = roleMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserModel userModel = userByUsername(username);
    if (Objects.isNull(userModel)) {
      throw new UsernameNotFoundException("cannot find user");
    }

    return new AuthUser(userModel);
  }

  @Override
  public Optional<UserDto> findUserByUserName(String userName) {

    UserModel userModel = userByUsername(userName);
    UserDto result = null;
    if (userModel != null) {
      result = mapper.modelToDto(userModel);
    }
    return Optional.ofNullable(result);
  }

  @Override
  public List<RoleDto> findUserRole(String userName) {

    UserModel userModel = userByUsername(userName);

    if (Objects.isNull(userModel)) {
      throw new UsernameNotFoundException("cannot find user");
    }

    return userModel.getUserRoles().stream().map((m) -> roleMapper.modelToDto(m.getRole())).toList();
  }

  private UserModel userByUsername(String username) {
    List<SpecificationFilter> filters = new ArrayList<>();

    filters
        .add(new SpecificationFilter("userName",
            username, SpecificationEnum.EQ,
            SpecificationFilter.JOIN_FILTER_AND));

    SpecificationBuilder<UserModel> specificationBuilder = new SpecificationBuilder<UserModel>()
        .setFilter(filters);

    Optional<UserModel> userModel = userRepository.findOne(specificationBuilder.buildSpecification());

    if (!userModel.isPresent()) {
      return null;
    }
    return userModel.get();
  }
}
