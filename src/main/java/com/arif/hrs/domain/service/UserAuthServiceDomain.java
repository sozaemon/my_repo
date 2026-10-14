package com.arif.hrs.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.UserDto;

public interface UserAuthServiceDomain extends UserDetailsService {
  public Optional<UserDto> findUserByUserName(String userName);

  public List<RoleDto> findUserRole(String userName);
}
