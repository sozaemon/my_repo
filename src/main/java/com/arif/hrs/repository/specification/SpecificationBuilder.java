package com.arif.hrs.repository.specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.service.serviceutility.FieldJoinMapping;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpecificationBuilder<T> {

  private List<SpecificationFilterWithJoin> filters = new ArrayList<>();

  Specification<T> specification = null;

  private List<FieldJoinMapping> joinMapping = new ArrayList<>();

  public SpecificationBuilder(List<SpecificationFilterWithJoin> filters) {
    this.filters = filters;
  }

  public SpecificationBuilder() {

  }

  public SpecificationBuilder<T> setFilter(List<SpecificationFilter> basicFilter) {
    this.filters = basicFilter.stream().map(m -> {
      return new SpecificationFilterWithJoin(
          m.getFieldName(),
          m.getValue(),
          m.getOperator(),
          m.getJoinFilter(),
          m.getValues());
    }).toList();
    return this;
  }

  public SpecificationBuilder<T> setMapping(List<FieldJoinMapping> joinMapping) {
    this.joinMapping = joinMapping;
    return this;
  }

  public Specification<T> buildSpecification() {

    log.info("build specification");

    for (SpecificationFilterWithJoin f : filters) {

      if (f == null || f.getFieldName() == null) {
        continue;
      }

      switch (f.getOperator()) {
        case SpecificationEnum.EQ:
          applySpecification(equalSpecification(f), f);
          break;
        case SpecificationEnum.NEQ:
          applySpecification(notEqualSpecification(f), f);
          break;
        case SpecificationEnum.LIKE:
          applySpecification(likeSpecification(f), f);
          break;
        case SpecificationEnum.GT:
          applySpecification(greaterThanSpecification(f), f);
          break;
        case SpecificationEnum.LT:
          applySpecification(lowerThanSpecification(f), f);
          break;
        case SpecificationEnum.NOTIN:
          applySpecification(notInSpecification(f), f);
          break;
        case SpecificationEnum.IN:
          applySpecification(inSpecification(f), f);
          break;
        case SpecificationEnum.NOTNULL:
          applySpecification(notNullSpecification(f), f);
          break;
        case SpecificationEnum.BETWEEN:
          applySpecification(betweenSpecification(f), f);
          break;
      }
    }
    if (this.specification == null) {
      return null;
    }

    Specification<T> builtSpecification = this.specification;
    return (root, query, builder) -> {
      query.distinct(true);

      List<String> alreadyJoinedTable = new ArrayList<>();

      for (FieldJoinMapping m : this.joinMapping) {
        if (!alreadyJoinedTable.contains(m.joinTable())) {
          root.fetch(m.joinTable(), JoinType.valueOf(m.joinType()));
          alreadyJoinedTable.add(m.joinTable());
        }
      }

      return builtSpecification.toPredicate(root, query, builder);
    };
  }

  private void applySpecification(Specification<T> spec, SpecificationFilterWithJoin filter) {
    if (spec == null) {
      return;
    }

    if (this.specification == null) {
      this.specification = Specification.where(spec);
    } else if (SpecificationFilter.JOIN_FILTER_OR.equals(filter.getJoinFilter())) {
      this.specification = this.specification.or(spec);
    } else {
      this.specification = this.specification.and(spec);
    }
  }

  public Specification<T> equalSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);
      return builder.equal(expression, f.getValue());
    };
  }

  public Specification<T> notEqualSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);
      return builder.notEqual(expression, f.getValue());
    };
  }

  public Specification<T> likeSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);
      return builder.like(builder.lower(expression.as(String.class)),
          "%" + String.valueOf(f.getValue()).toLowerCase() + "%");
    };
  }

  public Specification<T> greaterThanSpecification(SpecificationFilterWithJoin f) {

    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);

      switch (f.getValue()) {
        case Double doubleValue:
          return builder.greaterThan(expression.as(Double.class), doubleValue);
        case Integer integerValue:
          return builder.greaterThan(expression.as(Integer.class), integerValue);
        case Timestamp timeValue:
          return builder.greaterThan(expression.as(Timestamp.class), timeValue);
        default:
          break;
      }

      return null;
    };
  }

  public Specification<T> lowerThanSpecification(SpecificationFilterWithJoin f) {

    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);

      switch (f.getValue()) {
        case Double doubleValue:
          return builder.lessThan(expression.as(Double.class), doubleValue);
        case Integer integerValue:
          return builder.lessThan(expression.as(Integer.class), integerValue);
        case Timestamp timeValue:
          return builder.lessThan(expression.as(Timestamp.class), timeValue);
        default:
          break;
      }

      return null;
    };

  }

  public Specification<T> betweenSpecification(SpecificationFilterWithJoin f) {

    if (!f.getValues().isEmpty() && f.getValues().size() > 1) {

      return (root, query, builder) -> {
        Expression<?> expression = createExpressions(root, f);

        if (f.getValues().get(0) instanceof Double double1 && f.getValues().get(1) instanceof Double double2) {
          return builder.between(expression.as(Double.class), double1, double2);
        }
        if (f.getValues().get(0) instanceof Integer integer1 && f.getValues().get(1) instanceof Integer integer2) {
          return builder.between(expression.as(Integer.class), integer1, integer2);
        }
        if (f.getValues().get(0) instanceof Timestamp timeValue1
            && f.getValues().get(1) instanceof Timestamp timeValue2) {
          return builder.between(expression.as(Timestamp.class), timeValue1, timeValue2);
        }

        return null;
      };
    }
    return null;
  }

  public Specification<T> notInSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {

      Expression<?> expression = createExpressions(root, f);
      return builder.not(expression.in(f.getValues()));
    };
  }

  public Specification<T> inSpecification(SpecificationFilterWithJoin f) {

    return (root, query, builder) -> {

      if (!StringUtils.isEmpty(f.getJoinTable())) {
        return root.get(f.getJoinTable()).get(f.getJoinField()).in(f.getValues());
      }
      return root.get(f.getFieldName()).in(f.getValues());
    };
  }

  public Specification<T> notNullSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {
      Expression<?> expression = createExpressions(root, f);
      return builder.isNotNull(expression);
    };
  }

  private Expression<?> createExpressions(Root<T> root, SpecificationFilterWithJoin f) {
    Expression<?> expression = null;

    if (!StringUtils.isEmpty(f.getJoinTable())) {
      expression = root.get(f.getJoinTable()).get(f.getJoinField());
    } else {
      expression = root.get(f.getFieldName());
    }

    return expression;
  }
}
