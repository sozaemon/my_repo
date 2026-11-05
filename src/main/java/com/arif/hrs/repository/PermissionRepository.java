package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.PermissionModel;

public interface PermissionRepository
    extends JpaRepository<PermissionModel, Integer>, JpaSpecificationExecutor<PermissionModel> {
}
