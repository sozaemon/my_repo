package com.arif.hrs.domain.dto;

import lombok.Data;

@Data
public class AccessDto {
  private Integer id;
  private String name;
  private String path;
  private String method;
  private String description;
}
