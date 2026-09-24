package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.Role;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.model.RoleModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDto modelToDto(RoleModel model) {
        if ( model == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( model.getId() );
        roleDto.setCode( model.getCode() );
        roleDto.setName( model.getName() );
        roleDto.setDescription( model.getDescription() );
        roleDto.setActive( model.getActive() );

        return roleDto;
    }

    @Override
    public RoleModel dtoToModel(RoleDto dto) {
        if ( dto == null ) {
            return null;
        }

        RoleModel roleModel = new RoleModel();

        roleModel.setId( dto.getId() );
        roleModel.setCode( dto.getCode() );
        roleModel.setName( dto.getName() );
        roleModel.setDescription( dto.getDescription() );
        roleModel.setActive( dto.getActive() );

        return roleModel;
    }

    @Override
    public Role dtoToHttp(RoleDto dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( dto.getId() );
        role.setCode( dto.getCode() );
        role.setName( dto.getName() );
        role.setDescription( dto.getDescription() );
        role.setActive( dto.getActive() );

        return role;
    }

    @Override
    public RoleDto httpToDto(Role http) {
        if ( http == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( http.getId() );
        roleDto.setCode( http.getCode() );
        roleDto.setName( http.getName() );
        roleDto.setDescription( http.getDescription() );
        roleDto.setActive( http.getActive() );

        return roleDto;
    }
}
