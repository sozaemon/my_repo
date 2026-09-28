package com.arif.hrs.repository.specification.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;

import jakarta.persistence.criteria.Join;

public class RoleAccessSpecification {
  private RoleAccessSpecification() {
  }

  public static Specification<RoleAccessModel> filterRoleAccessByAccessId(Integer accessId) {
    return (root, query, builder) -> {
      Join<RoleAccessModel, AccessModel> access = root.join("access");

      return builder.and(builder.equal(access.get("id"), accessId));
    };
  }
}
