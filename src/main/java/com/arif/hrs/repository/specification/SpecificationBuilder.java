package com.arif.hrs.repository.specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class SpecificationBuilder<T> {

  private List<SpecificationFilterWithJoin> filters = new ArrayList<>();

  Specification<T> specification = null;

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

  public Specification<T> buildSpecification() {

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

      if (!StringUtils.isEmpty(f.getJoinTable())) {
        Join<?, ?> join = root.join(f.getJoinTable(), JoinType.valueOf(f.getJoinType()));
        return builder.equal(join.get(f.getJoinField()), f.getValue());
      }
      return builder.equal(root.get(f.getFieldName()), f.getValue());
    };
  }

  public Specification<T> notEqualSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {

      if (!StringUtils.isEmpty(f.getJoinTable())) {
        Join<?, ?> join = root.join(f.getJoinTable(), JoinType.valueOf(f.getJoinType()));
        return builder.notEqual(join.get(f.getJoinField()), f.getValue());
      }
      return builder.notEqual(root.get(f.getFieldName()), f.getValue());
    };
  }

  public Specification<T> likeSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {

      if (!StringUtils.isEmpty(f.getJoinTable())) {
        Join<?, ?> join = root.join(f.getJoinTable(), JoinType.valueOf(f.getJoinType()));
        return builder.like(join.get(f.getJoinField()), "%" + String.valueOf(f.getValue()).toLowerCase() + "%");
      }
      return builder.like(builder.lower(root.get(f.getFieldName())),
          "%" + String.valueOf(f.getValue()).toLowerCase() + "%");
    };
  }

  public Specification<T> greaterThanSpecification(SpecificationFilter f) {
    if (f.getValue() instanceof Double doubleValue) {
      return (root, query, builder) -> builder.greaterThan(root.get(f.getFieldName()), doubleValue);
    }

    if (f.getValue() instanceof Integer intValue) {
      return (root, query, builder) -> builder.greaterThan(root.get(f.getFieldName()), intValue);
    }

    if (f.getValue() instanceof Timestamp timeValue) {
      return (root, query, builder) -> builder.greaterThan(root.get(f.getFieldName()), timeValue);
    }

    return null;
  }

  public Specification<T> lowerThanSpecification(SpecificationFilter f) {
    if (f.getValue() instanceof Double doubleValue) {
      return (root, query, builder) -> builder.lessThan(root.get(f.getFieldName()), doubleValue);
    }

    if (f.getValue() instanceof Integer intValue) {
      return (root, query, builder) -> builder.lessThan(root.get(f.getFieldName()), intValue);
    }

    if (f.getValue() instanceof Timestamp timeValue) {
      return (root, query, builder) -> builder.lessThan(root.get(f.getFieldName()), timeValue);
    }
    return null;
  }

  public Specification<T> betweenSpecification(SpecificationFilter f) {
    if (!f.getValues().isEmpty() && f.getValues().size() > 1) {
      if (f.getValues().get(0) instanceof Double doubleValue1 && f.getValues().get(1) instanceof Double doubleValue2) {
        return (root, query, builder) -> builder.between(root.get(f.getFieldName()), doubleValue1, doubleValue2);
      } else if (f.getValues().get(0) instanceof Integer intValue1
          && f.getValues().get(1) instanceof Integer intValue2) {
        return (root, query, builder) -> builder.between(root.get(f.getFieldName()), intValue1, intValue2);
      } else if (f.getValues().get(0) instanceof Timestamp timeValue1
          && f.getValues().get(1) instanceof Timestamp timeValue2) {
        return (root, query, builder) -> builder.between(root.get(f.getFieldName()), timeValue1, timeValue2);
      }
    }
    return null;
  }

  public Specification<T> notInSpecification(SpecificationFilter f) {
    return (root, query, builder) -> builder.not(root.get(f.getFieldName()).in(f.getValues()));
  }

  public Specification<T> inSpecification(SpecificationFilter f) {
    return (root, query, builder) -> root.get(f.getFieldName()).in(f.getValues());
  }

  public Specification<T> notNullSpecification(SpecificationFilterWithJoin f) {
    return (root, query, builder) -> {
      if (!StringUtils.isEmpty(f.getJoinTable())) {
        Join<?, ?> join = root.join(f.getJoinTable(), JoinType.valueOf(f.getJoinType()));
        return builder.isNotNull(join.get(f.getJoinField()));
      }
      return builder.isNotNull(root.get(f.getFieldName()));
    };
  }
}
