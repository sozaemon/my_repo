package com.arif.hrs.domain.dto;

import com.arif.hrs.model.enums.AccessMethodEnum;
import com.arif.hrs.model.enums.UploadStatusEnum;

import lombok.Data;

@Data
public class RoleAccessUploadDto {
  private Integer id;
  private String name;
  private String path;
  private AccessMethodEnum method;
  private String description;
  private String roles;
  private UploadStatusEnum uploadStatus;
  private String errorMessage;
  private Integer uploadHeaderId;
}
