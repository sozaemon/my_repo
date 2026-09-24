package com.arif.hrs.domain.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageDto<T> {
  public List<T> data;
  public Integer size;
  public Integer page;
  public Long totalSize;

  public PageDto(Page<?> page, List<T> data) {
    this.data = data;
    this.size = page.getSize();
    this.page = page.getNumber();
    this.totalSize = page.getTotalElements();
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
