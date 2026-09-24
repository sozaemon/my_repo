package com.arif.hrs.auth.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.arif.hrs.controllers.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFailHandler implements AuthenticationEntryPoint {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    log.info("authorization fail {}", request.getRequestURI());

    response.setStatus(HTTPResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ResponseModel<?> model = new ResponseModel<String>(authException);

    response.getWriter().write(new ObjectMapper().writeValueAsString(model));
  }

}
