package com.arif.hrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import com.arif.hrs.model.AccessModel;

public interface AccessRepository extends JpaRepository<AccessModel, Integer>, JpaSpecificationExecutor<AccessModel> {

    @NativeQuery(value = "select * from sys_access "
            + " where method = :method and id in ( "
            + " select sys_access_id from sys_role_access a inner join sys_role r on a.sys_role_id = r.id where r.name in (:roleNames) "
            + ")")
    public List<AccessModel> findAccessByRoleAndPath(@Param("method") String method,
            @Param("roleNames") List<String> roleNames);
}
