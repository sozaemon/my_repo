package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.RoleAccessModel;

public interface RoleAccessRepository
    extends JpaRepository<RoleAccessModel, Integer>, JpaSpecificationExecutor<RoleAccessModel> {

}
