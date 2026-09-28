package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.AccessModel;

public interface AccessRepository extends JpaRepository<AccessModel, Integer>, JpaSpecificationExecutor<AccessModel> {

	// default List<AccessModel> findAccessByRoleAndPath2(String method,
	// List<String> roleNames) {

	// Specification<AccessModel> specification = (root, query, builder) -> {
	// Join<AccessModel, RoleAccessModel> roleAccess = root.join("roleAccess");
	// Join<RoleAccessModel, RoleModel> role = roleAccess.join("role");

	// query.distinct(true);

	// return builder.and(
	// builder.equal(root.get("method"), method),
	// role.get("name").in(roleNames));
	// };

	// return findAll(specification);
	// }
}
