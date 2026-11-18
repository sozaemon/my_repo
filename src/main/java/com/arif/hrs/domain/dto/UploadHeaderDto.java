package com.arif.hrs.domain.dto;

import java.sql.Timestamp;

import com.arif.hrs.model.enums.UploadHeaderStatusEnum;

import lombok.Data;

@Data
public class UploadHeaderDto {
  private Integer id;
  private String moduleName;
  private UploadHeaderStatusEnum status;
  private String description;
  private Timestamp created;
  private Timestamp updated;
}
