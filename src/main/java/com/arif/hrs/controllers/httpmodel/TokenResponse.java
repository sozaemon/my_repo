package com.arif.hrs.controllers.httpmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
  @JsonProperty("token")
  private String token;

  @JsonProperty("refreshToken")
  private String refreshToken;

  public TokenResponse(String token, String refreshToken) {
    this.token = token;
    this.refreshToken = refreshToken;
  }

}
