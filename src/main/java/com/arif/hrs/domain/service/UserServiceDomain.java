package com.arif.hrs.domain.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.arif.hrs.domain.dto.UserDto;

public interface UserServiceDomain extends UserDetailsService {
  public Optional<UserDto> findUserByUserName(String userName);
}
