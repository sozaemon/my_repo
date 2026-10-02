package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.ShiftModel;

public interface ShiftRepository extends JpaRepository<ShiftModel,Integer>, JpaSpecificationExecutor<ShiftModel>{
  
}
