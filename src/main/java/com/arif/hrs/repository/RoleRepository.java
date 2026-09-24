package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Integer>, JpaSpecificationExecutor<RoleModel> {

}
