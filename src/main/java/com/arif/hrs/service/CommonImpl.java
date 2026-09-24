package com.arif.hrs.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationFilter;

public class CommonImpl {
  private CommonImpl() {
  }

  public static <T> Specification<T> buildPaginationSpecification(PaginationDto paginationRequest) {
    List<SpecificationFilter> filters = paginationRequest.getFilters()
        .stream().map(m -> m.toSpecification())
        .toList();
    SpecificationBuilder<T> builder = new SpecificationBuilder<>(filters);

    return builder.buildSpecification();
  }

  public static Pageable buildPaginationPage(PaginationDto paginationRequest, String defaultSort) {
    Sort.Direction direction = paginationRequest.getSortDirection() != null
        && paginationRequest.getSortDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC
            : Sort.Direction.DESC;

    String field = paginationRequest.getSortBy();
    if (StringUtils.isEmpty(field)) {
      field = defaultSort;
    }
    Sort sort = Sort.by(direction, field);

    return PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), sort);

  }
}
