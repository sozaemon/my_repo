package com.arif.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arif.hrs.model.RoleAccessUploadModel;

public interface RoleAccessUploadRepository
        extends JpaRepository<RoleAccessUploadModel, Integer>, JpaSpecificationExecutor<RoleAccessUploadModel> {

    @Modifying
    @Query(value = """
                update u_role_access as u
                set uploadstatus = 'NOTVALID', errormessage='Duplicate Access'
                where u.path in
                    (select path from sys_access sa where sa.path = u.path and sa.method = u.method)
                and upload_header_id = :headerId
            """, nativeQuery = true)
    public void validateUploadAccessPathAndMethod(@Param("headerId") Integer headerId);
}
