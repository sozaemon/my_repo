package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.ShiftTypeModel;

public interface ShiftTypeRepository
    extends JpaRepository<ShiftTypeModel, Integer>, JpaSpecificationExecutor<ShiftTypeModel> {

}
