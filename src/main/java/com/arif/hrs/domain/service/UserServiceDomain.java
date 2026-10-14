package com.arif.hrs.domain.service;

import com.arif.hrs.domain.dto.FUserDto;
import com.arif.hrs.domain.dto.UserDto;

public interface UserServiceDomain extends CommonServiceDomain<UserDto, Integer> {
  public UserDto createUser(FUserDto fUserDto);

  public UserDto updatePassword(String userName, String oldPassword, String newPassword);
}
