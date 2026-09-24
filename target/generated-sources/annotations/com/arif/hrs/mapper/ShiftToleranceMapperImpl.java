package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.ShiftTolerance;
import com.arif.hrs.domain.dto.ShiftToleranceDto;
import com.arif.hrs.model.ShiftToleranceModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class ShiftToleranceMapperImpl implements ShiftToleranceMapper {

    @Override
    public ShiftToleranceModel dtoToModel(ShiftToleranceDto dto) {
        if ( dto == null ) {
            return null;
        }

        ShiftToleranceModel shiftToleranceModel = new ShiftToleranceModel();

        shiftToleranceModel.setId( dto.getId() );
        shiftToleranceModel.setCode( dto.getCode() );
        shiftToleranceModel.setToleranceName( dto.getToleranceName() );
        shiftToleranceModel.setToleranceBeforeUnit( dto.getToleranceBeforeUnit() );
        shiftToleranceModel.setToleranceAfterUnit( dto.getToleranceAfterUnit() );
        shiftToleranceModel.setToleranceBefore( dto.getToleranceBefore() );
        shiftToleranceModel.setToleranceAfter( dto.getToleranceAfter() );

        return shiftToleranceModel;
    }

    @Override
    public ShiftToleranceDto modelToDto(ShiftToleranceModel model) {
        if ( model == null ) {
            return null;
        }

        ShiftToleranceDto shiftToleranceDto = new ShiftToleranceDto();

        shiftToleranceDto.setId( model.getId() );
        shiftToleranceDto.setCode( model.getCode() );
        shiftToleranceDto.setToleranceName( model.getToleranceName() );
        shiftToleranceDto.setToleranceBeforeUnit( model.getToleranceBeforeUnit() );
        shiftToleranceDto.setToleranceAfterUnit( model.getToleranceAfterUnit() );
        shiftToleranceDto.setToleranceBefore( model.getToleranceBefore() );
        shiftToleranceDto.setToleranceAfter( model.getToleranceAfter() );

        return shiftToleranceDto;
    }

    @Override
    public ShiftTolerance dtoToHttp(ShiftToleranceDto dto) {
        if ( dto == null ) {
            return null;
        }

        ShiftTolerance shiftTolerance = new ShiftTolerance();

        shiftTolerance.setId( dto.getId() );
        shiftTolerance.setCode( dto.getCode() );
        shiftTolerance.setToleranceName( dto.getToleranceName() );
        shiftTolerance.setToleranceBeforeUnit( dto.getToleranceBeforeUnit() );
        shiftTolerance.setToleranceAfterUnit( dto.getToleranceAfterUnit() );
        shiftTolerance.setToleranceBefore( dto.getToleranceBefore() );
        shiftTolerance.setToleranceAfter( dto.getToleranceAfter() );

        return shiftTolerance;
    }

    @Override
    public ShiftToleranceDto httpToDto(ShiftTolerance http) {
        if ( http == null ) {
            return null;
        }

        ShiftToleranceDto shiftToleranceDto = new ShiftToleranceDto();

        shiftToleranceDto.setId( http.getId() );
        shiftToleranceDto.setCode( http.getCode() );
        shiftToleranceDto.setToleranceName( http.getToleranceName() );
        shiftToleranceDto.setToleranceBeforeUnit( http.getToleranceBeforeUnit() );
        shiftToleranceDto.setToleranceAfterUnit( http.getToleranceAfterUnit() );
        shiftToleranceDto.setToleranceBefore( http.getToleranceBefore() );
        shiftToleranceDto.setToleranceAfter( http.getToleranceAfter() );

        return shiftToleranceDto;
    }
}
