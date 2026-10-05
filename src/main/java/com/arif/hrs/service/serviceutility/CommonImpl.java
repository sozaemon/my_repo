package com.arif.hrs.service.serviceutility;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.domain.dto.FilterDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationFilterWithJoin;

public class CommonImpl {
  private CommonImpl() {
  }

  public static <T> Specification<T> buildPaginationSpecification(PaginationDto paginationRequest) {
    List<SpecificationFilterWithJoin> filters = paginationRequest.getFilters()
        .stream().map(SpecificationFilterWithJoin::new)
        .toList();
    SpecificationBuilder<T> builder = new SpecificationBuilder<>(filters);

    return builder.buildSpecification();
  }

  public static <T> Specification<T> buildFilterSpecificationDto(List<FilterDto> filterDto, Class<?> dtoClass) {
    List<SpecificationFilterWithJoin> filters = filterDto
        .stream().map(SpecificationFilterWithJoin::new)
        .toList();

    FilterValidator filterValidator = new FilterValidator(filters, dtoClass);

    SpecificationBuilder<T> builder = new SpecificationBuilder<>(filterValidator.buildAndValidateFilters());

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

  public static <T, R extends JpaSpecificationExecutor<T>> List<T> filterBySpecification(Specification<T> specification,
      R repository) {
    return repository.findAll(specification);
  }
}
