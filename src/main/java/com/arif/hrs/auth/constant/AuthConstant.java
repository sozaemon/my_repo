package com.arif.hrs.auth.constant;

public class AuthConstant {
  private AuthConstant() {
  }

  public static final String AUTH_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer";

  // JWT Additional Claims
  public static final String CLAIMS_EMAIL = "claims_email";

  public static final String JWT_KEY = "hrs-jwt-authentication-key";
  public static final String JWT_KEY_REFRESH = "hrs-jwt-authentication-key-refresh";

  public static final String REFRESH_REQUEST = "/refresh-token";
}
