package com.arif.hrs.controllers.httpmodel;

import java.util.List;

import com.arif.hrs.domain.dto.PageDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageResponse<T> {

  @JsonProperty("data")
  public List<T> data;
  @JsonProperty("size")
  public Integer size;
  @JsonProperty("page")
  public Integer page;
  @JsonProperty("total")
  public Long totalSize;

  public PageResponse() {
  }

  public PageResponse(PageDto<?> dto, List<T> data) {
    this.data = data;
    this.size = dto.getSize();
    this.page = dto.getPage();
    this.totalSize = dto.getTotalSize();
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(Long totalSize) {
    this.totalSize = totalSize;
  }

}
