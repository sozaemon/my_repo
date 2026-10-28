package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.RoleAccessUpload;
import com.arif.hrs.domain.dto.RoleAccessUploadDto;
import com.arif.hrs.model.RoleAccessUploadModel;

@Mapper(componentModel = "spring")
public interface RoleAccessUploadMapper {
  @Mapping(source = "uploadProperty.uploadStatus", target = "uploadStatus")
  @Mapping(source = "uploadProperty.errorMessage", target = "errorMessage")
  RoleAccessUploadDto modelToDto(RoleAccessUploadModel model);

  @Mapping(source = "uploadStatus", target = "uploadProperty.uploadStatus")
  @Mapping(source = "errorMessage", target = "uploadProperty.errorMessage")
  RoleAccessUploadModel dtoToModel(RoleAccessUploadDto dto);

  RoleAccessUpload dtoToHttp(RoleAccessUploadDto dto);

  RoleAccessUploadDto httpToDto(RoleAccessUpload http);
}
