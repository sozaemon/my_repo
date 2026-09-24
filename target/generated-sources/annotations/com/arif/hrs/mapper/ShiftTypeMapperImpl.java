package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.ShiftType;
import com.arif.hrs.domain.dto.ShiftTypeDto;
import com.arif.hrs.model.ShiftTypeModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class ShiftTypeMapperImpl implements ShiftTypeMapper {

    @Override
    public ShiftTypeDto modelToDto(ShiftTypeModel model) {
        if ( model == null ) {
            return null;
        }

        ShiftTypeDto shiftTypeDto = new ShiftTypeDto();

        shiftTypeDto.setId( model.getId() );
        shiftTypeDto.setCode( model.getCode() );
        shiftTypeDto.setName( model.getName() );

        return shiftTypeDto;
    }

    @Override
    public ShiftTypeModel dtoToModel(ShiftTypeDto dto) {
        if ( dto == null ) {
            return null;
        }

        ShiftTypeModel shiftTypeModel = new ShiftTypeModel();

        shiftTypeModel.setId( dto.getId() );
        shiftTypeModel.setCode( dto.getCode() );
        shiftTypeModel.setName( dto.getName() );

        return shiftTypeModel;
    }

    @Override
    public ShiftType dtoToHttp(ShiftTypeDto dto) {
        if ( dto == null ) {
            return null;
        }

        ShiftType shiftType = new ShiftType();

        shiftType.setId( dto.getId() );
        shiftType.setCode( dto.getCode() );
        shiftType.setName( dto.getName() );

        return shiftType;
    }

    @Override
    public ShiftTypeDto httpToDto(ShiftType http) {
        if ( http == null ) {
            return null;
        }

        ShiftTypeDto shiftTypeDto = new ShiftTypeDto();

        shiftTypeDto.setId( http.getId() );
        shiftTypeDto.setCode( http.getCode() );
        shiftTypeDto.setName( http.getName() );

        return shiftTypeDto;
    }
}
