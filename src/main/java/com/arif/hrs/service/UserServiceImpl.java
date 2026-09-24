package com.arif.hrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.mapper.UserMapper;
import com.arif.hrs.model.UserModel;
import com.arif.hrs.repository.UserRepository;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationEnum;
import com.arif.hrs.repository.specification.SpecificationFilter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceDomain {

  private final UserRepository userRepository;
  private final UserMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<SpecificationFilter> filters = new ArrayList<>();

    filters
        .add(new SpecificationFilter("userName", username, SpecificationEnum.EQ, SpecificationFilter.JOIN_FILTER_AND));

    SpecificationBuilder<UserModel> specificationBuilder = new SpecificationBuilder<UserModel>(filters);

    Optional<UserModel> userModel = userRepository.findOne(specificationBuilder.buildSpecification());

    if (!userModel.isPresent()) {
      throw new UsernameNotFoundException("cannot find user");
    }

    return new AuthUser(userModel.get());
  }

  @Override
  public Optional<UserDto> findUserByUserName(String userName) {
    List<SpecificationFilter> filters = new ArrayList<>();

    filters
        .add(new SpecificationFilter("userName", userName, SpecificationEnum.EQ, SpecificationFilter.JOIN_FILTER_AND));

    SpecificationBuilder<UserModel> specificationBuilder = new SpecificationBuilder<UserModel>(filters);

    Optional<UserModel> userModel = userRepository.findOne(specificationBuilder.buildSpecification());

    UserDto result = null;
    if (userModel.isPresent()) {
      result = mapper.modelToDto(userModel.get());
    }
    return Optional.ofNullable(result);
  }

}
