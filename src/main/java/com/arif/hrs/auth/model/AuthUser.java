package com.arif.hrs.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.arif.hrs.model.UserModel;

public class AuthUser implements UserDetails {

  private String userName;
  private String password;
  private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

  public AuthUser(UserModel userModel) {
    this.userName = userModel.getUserName();
    this.password = userModel.getPassword();

    this.authorities = userModel.getUserRoles().stream()
        .map(r -> new SimpleGrantedAuthority(r.getRole().getName()))
        .toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public @Nullable String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

}
