package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.AccessModel;

public interface AccessRepository extends JpaRepository<AccessModel, Integer>, JpaSpecificationExecutor<AccessModel> {

}
