package com.arif.hrs.controllers.httpmodel;

import com.arif.hrs.model.enums.UploadHeaderStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadHeader {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("moduleName")
  private String moduleName;
  @JsonProperty("status")
  private UploadHeaderStatusEnum status;
  @JsonProperty("description")
  private String description;
}
