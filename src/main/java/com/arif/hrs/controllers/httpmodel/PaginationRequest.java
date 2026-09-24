package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaginationRequest {
  @JsonProperty("page")
  private Integer page;
  @JsonProperty("pageSize")
  private Integer pageSize;
  @JsonProperty("filter")
  private List<FilterRequest> filterRequest;
  @JsonProperty("sortBy")
  private String sortBy;
  @JsonProperty("sortDirection")
  private String sortDirection;

}
