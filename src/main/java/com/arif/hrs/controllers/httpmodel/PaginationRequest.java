package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PaginationRequest {
  @JsonProperty("page")
  @PositiveOrZero(message = "page must be zero or greater")
  private Integer page;
  @JsonProperty("pageSize")
  @Positive(message = "pageSize must be positive")
  private Integer pageSize;
  @JsonProperty("filter")
  @Valid
  private List<FilterRequest> filterRequest;
  @JsonProperty("sortBy")
  private String sortBy;
  @JsonProperty("sortDirection")
  private String sortDirection;

}
