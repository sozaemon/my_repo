package com.arif.hrs.domain.service;

import java.util.Optional;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;

import jakarta.persistence.EntityNotFoundException;

public interface CommonServiceDomain<T, K> {
  public Optional<T> getOne(K id);

  public T create(T v);

  public T update(T v) throws EntityNotFoundException;

  public void delete(K id) throws EntityNotFoundException;

  public PageDto<T> paginate(PaginationDto paginationRequest);

}
