package com.arif.hrs.mapper;

import org.mapstruct.Mapper;

import com.arif.hrs.controllers.httpmodel.UploadHeader;
import com.arif.hrs.domain.dto.UploadHeaderDto;
import com.arif.hrs.model.UploadHeaderModel;

@Mapper(componentModel = "spring")
public interface UploadHeaderMapper {

  UploadHeaderDto modelToDto(UploadHeaderModel model);

  UploadHeaderModel dtoToModel(UploadHeaderDto dto);

  UploadHeader dtoToHttp(UploadHeaderDto dto);
}
