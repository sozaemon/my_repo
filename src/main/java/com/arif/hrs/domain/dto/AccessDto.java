package com.arif.hrs.domain.dto;

import com.arif.hrs.model.enums.AccessMethodEnum;

import lombok.Data;

@Data
public class AccessDto {
  private Integer id;
  private String name;
  private String path;
  private AccessMethodEnum method;
  private String description;
}
