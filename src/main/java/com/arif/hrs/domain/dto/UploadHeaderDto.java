package com.arif.hrs.domain.dto;

import com.arif.hrs.model.enums.UploadHeaderStatusEnum;

import lombok.Data;

@Data
public class UploadHeaderDto {
  private Integer id;
  private String moduleName;
  private UploadHeaderStatusEnum status;
  private String description;
}
