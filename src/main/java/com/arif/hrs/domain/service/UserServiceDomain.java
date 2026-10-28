package com.arif.hrs.domain.service;

import java.util.Optional;

import com.arif.hrs.domain.dto.FUserDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.UserDto;

import jakarta.persistence.EntityNotFoundException;

public interface UserServiceDomain {

  public Optional<UserDto> getOne(Integer id);

  public UserDto update(UserDto v) throws EntityNotFoundException;

  public void delete(Integer id) throws EntityNotFoundException;

  public PageDto<UserDto> paginate(PaginationDto paginationRequest);

  public UserDto createUser(FUserDto fUserDto);

  public UserDto updatePassword(String userName, String oldPassword, String newPassword);
}
