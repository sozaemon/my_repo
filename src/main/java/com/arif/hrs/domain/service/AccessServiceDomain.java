package com.arif.hrs.domain.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.arif.hrs.domain.dto.AccessDto;

public interface AccessServiceDomain extends CommonServiceDomain<AccessDto, Integer> {
  List<AccessDto> getAccessByRolesAndPathAndMethod(List<String> roleName, String method);

  ByteArrayInputStream exportToExcel();
}
