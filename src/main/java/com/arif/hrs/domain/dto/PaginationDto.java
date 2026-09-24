package com.arif.hrs.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.arif.hrs.controllers.httpmodel.PaginationRequest;

public class PaginationDto {

  private Integer page;
  private Integer pageSize;
  private List<FilterDto> filters = new ArrayList<>();
  private String sortBy;
  private String sortDirection;

  public PaginationDto() {
  }

  public PaginationDto(PaginationRequest request) {
    this.page = request.getPage();
    this.pageSize = request.getPageSize();
    if (request.getFilterRequest() != null) {
      this.filters = request.getFilterRequest().stream().map(FilterDto::new).toList();
    }

    this.sortBy = request.getSortBy();
    this.sortDirection = request.getSortDirection();
  }

  public Integer getPage() {
    if (page == null) {
      return Integer.valueOf(0);
    }
    if (page <= 0) {
      return page;
    }
    return page - 1;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getPageSize() {
    if (pageSize == null) {
      return Integer.valueOf(10);
    }
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public List<FilterDto> getFilters() {
    return filters;
  }

  public void setFilters(List<FilterDto> filters) {
    this.filters = filters;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public String getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(String sortDirection) {
    this.sortDirection = sortDirection;
  }

}
