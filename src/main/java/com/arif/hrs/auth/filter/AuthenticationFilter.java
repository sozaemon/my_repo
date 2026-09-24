package com.arif.hrs.auth.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arif.hrs.auth.constant.AuthConstant;
import com.arif.hrs.auth.jwt.AuthJwtService;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.util.cookie.CookieComponent;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthJwtService jwtService;
  private final UserServiceDomain userService;
  private final CookieComponent cookieComponent;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    // Skip filtering for your public endpoints
    return path.startsWith("/api/auth/") && !path.endsWith(AuthConstant.REFRESH_REQUEST);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("authenticate request with token");

    String token = retrieveAuthenticationToken(request);
    String username = null;

    if (token != null) {
      try {
        username = jwtService.extractUserName(token);
      } catch (Exception e) {
        log.error("fetch username from token fail", e);
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails user = userService.loadUserByUsername(username);
      Boolean tokenIsValid = jwtService.validateToken(token, user);

      if (tokenIsValid.booleanValue()) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities());
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
