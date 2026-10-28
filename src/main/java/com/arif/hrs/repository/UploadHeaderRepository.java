package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.arif.hrs.model.UploadHeaderModel;

public interface UploadHeaderRepository
    extends JpaRepository<UploadHeaderModel, Integer>, JpaSpecificationExecutor<UploadHeaderModel> {

}
