package com.arif.hrs.mapper;

import com.arif.hrs.controllers.httpmodel.User;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.model.UserModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-23T13:20:33+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12.1 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto modelToDto(UserModel model) {
        if ( model == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( model.getId() );
        userDto.setUserName( model.getUserName() );
        userDto.setEmail( model.getEmail() );
        userDto.setExpiryDate( model.getExpiryDate() );
        userDto.setFullName( model.getFullName() );

        return userDto;
    }

    @Override
    public UserModel dtoToModel(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setId( dto.getId() );
        userModel.setUserName( dto.getUserName() );
        userModel.setEmail( dto.getEmail() );
        userModel.setExpiryDate( dto.getExpiryDate() );
        userModel.setFullName( dto.getFullName() );

        return userModel;
    }

    @Override
    public User dtoToHttp(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setUserName( dto.getUserName() );
        user.setEmail( dto.getEmail() );
        user.setExpiryDate( dto.getExpiryDate() );
        user.setFullName( dto.getFullName() );

        return user;
    }

    @Override
    public UserDto httpToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setUserName( user.getUserName() );
        userDto.setEmail( user.getEmail() );
        userDto.setExpiryDate( user.getExpiryDate() );
        userDto.setFullName( user.getFullName() );

        return userDto;
    }
}
