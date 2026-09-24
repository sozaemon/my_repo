package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.Access;
import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.model.AccessModel;
import com.arif.hrs.util.excel.excelmodel.AccessExcelModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class AccessMapperImpl implements AccessMapper {

    @Override
    public Access dtoToHttp(AccessDto dto) {
        if ( dto == null ) {
            return null;
        }

        Access access = new Access();

        access.setId( dto.getId() );
        access.setName( dto.getName() );
        access.setPath( dto.getPath() );
        access.setMethod( dto.getMethod() );
        access.setDescription( dto.getDescription() );

        return access;
    }

    @Override
    public AccessModel dtoToModel(AccessDto dto) {
        if ( dto == null ) {
            return null;
        }

        AccessModel accessModel = new AccessModel();

        accessModel.setId( dto.getId() );
        accessModel.setName( dto.getName() );
        accessModel.setPath( dto.getPath() );
        accessModel.setMethod( dto.getMethod() );
        accessModel.setDescription( dto.getDescription() );

        return accessModel;
    }

    @Override
    public AccessDto modelToDto(AccessModel model) {
        if ( model == null ) {
            return null;
        }

        AccessDto accessDto = new AccessDto();

        accessDto.setId( model.getId() );
        accessDto.setName( model.getName() );
        accessDto.setPath( model.getPath() );
        accessDto.setMethod( model.getMethod() );
        accessDto.setDescription( model.getDescription() );

        return accessDto;
    }

    @Override
    public AccessDto httpToDto(Access http) {
        if ( http == null ) {
            return null;
        }

        AccessDto accessDto = new AccessDto();

        accessDto.setId( http.getId() );
        accessDto.setName( http.getName() );
        accessDto.setPath( http.getPath() );
        accessDto.setMethod( http.getMethod() );
        accessDto.setDescription( http.getDescription() );

        return accessDto;
    }

    @Override
    public AccessExcelModel modelToExcel(AccessModel dto) {
        if ( dto == null ) {
            return null;
        }

        AccessExcelModel accessExcelModel = new AccessExcelModel();

        accessExcelModel.setName( dto.getName() );
        accessExcelModel.setPath( dto.getPath() );
        accessExcelModel.setMethod( dto.getMethod() );
        accessExcelModel.setDescription( dto.getDescription() );

        return accessExcelModel;
    }
}
