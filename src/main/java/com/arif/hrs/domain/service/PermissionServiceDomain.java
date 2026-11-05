package com.arif.hrs.domain.service;

import java.util.List;

import com.arif.hrs.domain.dto.PermissionDto;

public interface PermissionServiceDomain extends CommonServiceDomain<PermissionDto, Integer> {
  List<PermissionDto> getPermissionByRole(String roleCode);
}
