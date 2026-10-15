package com.arif.hrs.domain.service;

import java.util.Optional;

import com.arif.hrs.domain.dto.RoleDto;

public interface RoleServiceDomain extends CommonServiceDomain<RoleDto, Integer> {
  Optional<RoleDto> findByName(String roleName);
}
