package com.arif.hrs.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;

import jakarta.persistence.criteria.Join;

public interface AccessRepository extends JpaRepository<AccessModel, Integer>, JpaSpecificationExecutor<AccessModel> {

	@NativeQuery(value = "select * from sys_access "
			+ " where method = :method and id in ( "
			+ " select sys_access_id from sys_role_access a inner join sys_role r on a.sys_role_id = r.id where r.name in (:roleNames) "
			+ ")")
	public List<AccessModel> findAccessByRoleAndPath(@Param("method") String method,
			@Param("roleNames") List<String> roleNames);

	default List<AccessModel> findAccessByRoleAndPath2(String method, List<String> roleNames) {

		Specification<AccessModel> specification = (root, query, builder) -> {
			Join<AccessModel, RoleAccessModel> roleAccess = root.join("roleAccess");
			Join<RoleAccessModel, RoleModel> role = roleAccess.join("role");

			query.distinct(true);

			return builder.and(
					builder.equal(root.get("method"), method),
					role.get("name").in(roleNames));
		};

		return findAll(specification);
	}
}
