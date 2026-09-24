package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.ShiftToleranceModel;

public interface ShiftToleranceRepository
    extends JpaRepository<ShiftToleranceModel, Integer>, JpaSpecificationExecutor<ShiftToleranceModel> {

}
