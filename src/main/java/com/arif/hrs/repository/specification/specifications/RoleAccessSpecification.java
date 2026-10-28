package com.arif.hrs.repository.specification.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleAccessUploadModel;
import com.arif.hrs.model.UploadHeaderModel;
import com.arif.hrs.model.enums.UploadStatusEnum;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class RoleAccessSpecification {
  private RoleAccessSpecification() {
  }

  public static Specification<RoleAccessModel> filterRoleAccessByAccessId(Integer accessId) {
    return (root, query, builder) -> {
      Join<RoleAccessModel, AccessModel> access = root.join("access");

      return builder.and(builder.equal(access.get("id"), accessId));
    };
  }

  public static Specification<RoleAccessUploadModel> findUploadDataByHeaderId(Integer headerId) {
    return (root, query, builder) -> {
      Join<RoleAccessUploadModel, UploadHeaderModel> header = root.join("uploadHeader", JoinType.INNER);

      return builder
          .and(builder.equal(header.get("id"), headerId),
              builder.equal(root.get("uploadProperty").get("uploadstatus"), UploadStatusEnum.VALID));

    };
  }
}
