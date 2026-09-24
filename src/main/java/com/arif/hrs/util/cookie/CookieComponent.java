package com.arif.hrs.util.cookie;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieComponent {
  @Value("${app.cookie.lifetime}")
  private Long cookieLifetime;

  @Value("${app.cookie.samesite}")
  private String cookieSameSite;

  private String key;
  private String value;

  public CookieComponent setCookie(String key, String value) {
    this.key = key;
    this.value = value;
    return this;
  }

  public ResponseCookie build() {
    return ResponseCookie.from(this.key, this.value)
        .httpOnly(true)
        .secure(true)
        .sameSite(cookieSameSite)
        .path("/")
        .maxAge(cookieLifetime)
        .build();
  }

  public ResponseCookie clear() {
    return ResponseCookie.from(this.key, Strings.EMPTY)
        .httpOnly(true)
        .secure(true)
        .sameSite(cookieSameSite)
        .path("/")
        .maxAge(0)
        .build();
  }

  public String retrieveCookie(HttpServletRequest request, String cookieKey){
    Cookie[] cookies = request.getCookies();

    String result = null;

    if (cookies != null){
      for(Cookie c: cookies){
        if (cookieKey.equals(c.getName())){
          result = c.getValue();
        }
      }
    }
    return result;
  }
}
