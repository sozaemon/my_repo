package com.arif.hrs.domain.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.domain.dto.RoleAccessUploadDto;
import com.arif.hrs.domain.dto.UploadHeaderDto;

public interface RoleAccessServiceDomain extends CommonServiceDomain<RoleAccessDto, Integer> {
  public List<RoleAccessDto> findByAccessId(Integer accessId);

  public List<RoleAccessDto> assignRolesToAccess(List<Integer> roleIds, Integer accessId);

  public UploadHeaderDto uploadRoleAccess(MultipartFile file, String description);

  public PageDto<RoleAccessUploadDto> pageUpload(PaginationDto pageRequest);

  public RoleAccessUploadDto updateUpload(RoleAccessUploadDto uploadData);

  public void deleteRoleAccessUpload(Integer uploadId);

  public void processUploadData(Integer uploadHeaderId);

}
