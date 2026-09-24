package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.RoleAccess;
import com.arif.hrs.domain.dto.RoleAccessDto;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.model.RoleAccessModel;
import com.arif.hrs.model.RoleModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class RoleAccessMapperImpl implements RoleAccessMapper {

    @Override
    public RoleAccessDto modelToDto(RoleAccessModel model) {
        if ( model == null ) {
            return null;
        }

        RoleAccessDto roleAccessDto = new RoleAccessDto();

        roleAccessDto.setId( model.getId() );
        roleAccessDto.setAccessId( modelAccessId( model ) );
        roleAccessDto.setAccessName( modelAccessName( model ) );
        roleAccessDto.setRoleId( modelRoleId( model ) );
        roleAccessDto.setRoleName( modelRoleName( model ) );

        return roleAccessDto;
    }

    @Override
    public RoleAccessModel dtoToModel(RoleAccessDto dto) {
        if ( dto == null ) {
            return null;
        }

        RoleAccessModel roleAccessModel = new RoleAccessModel();

        roleAccessModel.setAccess( roleAccessDtoToAccessModel( dto ) );
        roleAccessModel.setRole( roleAccessDtoToRoleModel( dto ) );
        roleAccessModel.setId( dto.getId() );

        return roleAccessModel;
    }

    @Override
    public RoleAccess dtoToHttp(RoleAccessDto dto) {
        if ( dto == null ) {
            return null;
        }

        RoleAccess roleAccess = new RoleAccess();

        roleAccess.setId( dto.getId() );
        roleAccess.setAccessId( dto.getAccessId() );
        roleAccess.setAccessName( dto.getAccessName() );
        roleAccess.setRoleId( dto.getRoleId() );
        roleAccess.setRoleName( dto.getRoleName() );

        return roleAccess;
    }

    @Override
    public RoleAccessDto httpToDto(RoleAccess http) {
        if ( http == null ) {
            return null;
        }

        RoleAccessDto roleAccessDto = new RoleAccessDto();

        roleAccessDto.setId( http.getId() );
        roleAccessDto.setAccessId( http.getAccessId() );
        roleAccessDto.setAccessName( http.getAccessName() );
        roleAccessDto.setRoleId( http.getRoleId() );
        roleAccessDto.setRoleName( http.getRoleName() );

        return roleAccessDto;
    }

    private Integer modelAccessId(RoleAccessModel roleAccessModel) {
        AccessModel access = roleAccessModel.getAccess();
        if ( access == null ) {
            return null;
        }
        return access.getId();
    }

    private String modelAccessName(RoleAccessModel roleAccessModel) {
        AccessModel access = roleAccessModel.getAccess();
        if ( access == null ) {
            return null;
        }
        return access.getName();
    }

    private Integer modelRoleId(RoleAccessModel roleAccessModel) {
        RoleModel role = roleAccessModel.getRole();
        if ( role == null ) {
            return null;
        }
        return role.getId();
    }

    private String modelRoleName(RoleAccessModel roleAccessModel) {
        RoleModel role = roleAccessModel.getRole();
        if ( role == null ) {
            return null;
        }
        return role.getName();
    }

    protected AccessModel roleAccessDtoToAccessModel(RoleAccessDto roleAccessDto) {
        if ( roleAccessDto == null ) {
            return null;
        }

        AccessModel accessModel = new AccessModel();

        accessModel.setId( roleAccessDto.getAccessId() );

        return accessModel;
    }

    protected RoleModel roleAccessDtoToRoleModel(RoleAccessDto roleAccessDto) {
        if ( roleAccessDto == null ) {
            return null;
        }

        RoleModel roleModel = new RoleModel();

        roleModel.setId( roleAccessDto.getRoleId() );

        return roleModel;
    }
}
