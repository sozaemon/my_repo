package com.arif.hrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.UserRoleModel;

public interface UserRoleRepository
    extends JpaRepository<UserRoleModel, Integer>, JpaSpecificationExecutor<UserRoleModel> {

  List<UserRoleModel> findByUserId(Integer userId);
}
