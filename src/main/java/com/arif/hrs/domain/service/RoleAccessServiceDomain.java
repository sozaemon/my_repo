package com.arif.hrs.domain.service;

import java.util.List;

import com.arif.hrs.domain.dto.RoleAccessDto;

public interface RoleAccessServiceDomain extends CommonServiceDomain<RoleAccessDto, Integer> {
  public List<RoleAccessDto> findByAccessId(Integer accessId);
}
