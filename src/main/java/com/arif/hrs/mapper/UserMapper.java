package com.arif.hrs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arif.hrs.controllers.httpmodel.User;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.model.UserModel;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "expiryDate", target = "expiryDate")
  @Mapping(source = "fullName", target = "fullName")
  UserDto modelToDto(UserModel model);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "expiryDate", target = "expiryDate")
  @Mapping(source = "fullName", target = "fullName")
  UserModel dtoToModel(UserDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "expiryDate", target = "expiryDate")
  @Mapping(source = "fullName", target = "fullName")
  User dtoToHttp(UserDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "userName", target = "userName")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "expiryDate", target = "expiryDate")
  @Mapping(source = "fullName", target = "fullName")
  UserDto httpToDto(User user);
}
