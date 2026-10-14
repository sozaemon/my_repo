package com.arif.hrs.auth.filter;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arif.hrs.auth.constant.AuthConstant;
import com.arif.hrs.auth.jwt.AuthJwtService;
import com.arif.hrs.domain.service.UserAuthServiceDomain;
import com.arif.hrs.util.cookie.CookieComponent;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthJwtService jwtService;
  private final UserAuthServiceDomain userAuthService;
  private final CookieComponent cookieComponent;

  public AuthenticationFilter(AuthJwtService jwtService, UserAuthServiceDomain userAuthService,
      CookieComponent cookieComponent) {
    this.jwtService = jwtService;
    this.userAuthService = userAuthService;
    this.cookieComponent = cookieComponent;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    // Skip filtering for your public

    if (Arrays
        .asList("/api/auth/login",
            "/api/auth/logout",
            "/api/auth/user-roles",
            "/api-docs",
            "/swagger-ui/index.html")
        .contains(path)) {
      return true;
    }

    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("authenticate request with token");

    String token = retrieveAuthenticationToken(request);
    String username = null;
    String role = null;

    if (token != null) {
      try {
        username = jwtService.extractUserName(token);
        role = (String) jwtService.extractAdditionalClaims(token, AuthConstant.CLAIMS_ROLE);
      } catch (Exception e) {
        log.error("fetch username from token fail", e);
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails user = userAuthService.loadUserByUsername(username);
      Boolean tokenIsValid = jwtService.validateToken(token, user);

      if (role == null) {
        role = "UNKNOWN_ROLE";
      }

      if (tokenIsValid.booleanValue()) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            user, null, Arrays.asList(new SimpleGrantedAuthority(role)));
        authToken.setDetails(
            new WebAuthenticationDetailsSource()
                .buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }

  private String retrieveAuthenticationToken(HttpServletRequest request) {

    // prioritize token from cookie
    String token = retrieveAuthTokenFromCookie(request);

    if (StringUtils.isEmpty(token)) {
      token = retrieveAuthTokenFromHeader(request);
    }
    return token;
  }

  private String retrieveAuthTokenFromHeader(HttpServletRequest request) {
    final String token = request.getHeader(AuthConstant.AUTH_HEADER);
    if (StringUtils.isEmpty(token) == Boolean.FALSE.booleanValue()) {
      return jwtService.fetchTokenValue(token);
    }
    return null;
  }

  // retrieve authentication from cookie
  private String retrieveAuthTokenFromCookie(HttpServletRequest request) {

    String authCookieKey = AuthConstant.JWT_KEY;

    if (request.getRequestURI().equals("/api/auth" + AuthConstant.REFRESH_REQUEST)) {
      authCookieKey = AuthConstant.JWT_KEY_REFRESH;
    }

    String cookieValue = cookieComponent.retrieveCookie(request, authCookieKey);

    return jwtService.fetchTokenValue(cookieValue);
  }
}
