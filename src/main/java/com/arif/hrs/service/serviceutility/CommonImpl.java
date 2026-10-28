package com.arif.hrs.service.serviceutility;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.domain.dto.FilterDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.repository.specification.SpecificationBuilder;
import com.arif.hrs.repository.specification.SpecificationFilterWithJoin;

@SuppressWarnings("hiding")
public class CommonImpl {
  private CommonImpl() {
  }

  /**
   * build specification using pagination dto
   * this method using pagination dto properties filters to generate
   * specification for generic type
   * 
   * @deprecated()
   *               it will be replace with with
   *               {@link #buildFilterSpecificationDto(filterDto, dtoClass)}
   *               this new method implement specification join that defined in
   *               dto class
   * @param <T>
   * @param paginationRequest
   * @return
   */
  @Deprecated(forRemoval = true)
  public static <T> Specification<T> buildPaginationSpecification(PaginationDto paginationRequest) {
    List<SpecificationFilterWithJoin> filters = paginationRequest.getFilters()
        .stream().map(SpecificationFilterWithJoin::new)
        .toList();
    SpecificationBuilder<T> builder = new SpecificationBuilder<>(filters);

    return builder.buildSpecification();
  }

  /**
   * build specification with generic type Model
   * using {@link com.arif.hrs.domain.dto.FilterDto} as filter parameter
   * {@link com.arif.hrs.service.serviceutility.FilterValidator} to mapping join
   * table
   * join table bind in dto class
   * 
   * @param <T>
   * @param filterDto
   * @param dtoClass
   * @return
   */
  public static <T> Specification<T> buildFilterSpecificationDto(List<FilterDto> filterDto, Class<?> dtoClass) {
    List<SpecificationFilterWithJoin> filters = filterDto
        .stream().map(SpecificationFilterWithJoin::new)
        .toList();

    FilterValidator filterValidator = new FilterValidator(filters, dtoClass);

    SpecificationBuilder<T> builder = new SpecificationBuilder<>(filterValidator.buildAndValidateFilters());
    builder.setMapping(filterValidator.extractMappingProperty());

    return builder.buildSpecification();
  }

  /**
   * build pagination Page using pagination dto
   * 
   * @deprecated()
   *               will be replace with
   *               {@link #buildPaginationPage(paginationRequest, defaultSort, dtoClass)}
   *               this new method implement specification join that defined in
   *               dto class
   * @param paginationRequest
   * @param defaultSort
   * @return
   */
  @Deprecated(forRemoval = true)
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

  /**
   * build pagination Page using {@link com.arif.hrs.domain.dto.PaginationDto}
   * {@link com.arif.hrs.service.serviceutility.FilterValidator} to mapping
   * join table bind in dto class
   * 
   * @param paginationRequest
   * @param defaultSort
   * @param dtoClass
   * @return
   */
  public static Pageable buildPaginationPage(PaginationDto paginationRequest, String defaultSort, Class<?> dtoClass) {

    Sort.Direction direction = paginationRequest.getSortDirection() != null
        && paginationRequest.getSortDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC
            : Sort.Direction.DESC;

    final String sort = StringUtils.isEmpty(paginationRequest.getSortBy()) ? defaultSort
        : paginationRequest.getSortBy();

    Sort sortOption = Sort.by(direction, sort);

    FilterValidator filterValidator = new FilterValidator(dtoClass);
    Optional<FieldJoinMapping> mappings = filterValidator.extractMappingProperty().stream()
        .filter(f -> f.fieldName().equals(sort)).findFirst();

    if (mappings.isPresent()) {
      FieldJoinMapping map = mappings.get();
      sortOption = Sort.by(direction, String.join(".", map.joinTable(), map.targetColumn()));
    }

    return PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), sortOption);

  }

  public static <T, R extends JpaSpecificationExecutor<T>> List<T> filterBySpecification(Specification<T> specification,
      R repository) {
    return repository.findAll(specification);
  }
}
