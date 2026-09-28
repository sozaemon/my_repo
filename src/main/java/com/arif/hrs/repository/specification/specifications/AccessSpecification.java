package com.arif.hrs.repository.specification.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;

import jakarta.persistence.criteria.Join;

public class AccessSpecification {
  private AccessSpecification() {
  }

  public static Specification<AccessModel> filterByRoleNameAndMethod(List<String> roleNames, String method) {
    return (root, query, builder) -> {
      Join<AccessModel, RoleAccessModel> roleAccess = root.join("roleAccess");
      Join<RoleAccessModel, RoleModel> role = roleAccess.join("role");

      query.distinct(true);

      return builder.and(builder.equal(root.get("method"), method),
          role.get("name").in(roleNames));
    };
  }
}
