package com.arif.hrs.auth.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.arif.hrs.controllers.ResponseModel;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class NoAccessHandler implements AccessDeniedHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.info("no access handler {}", request.getRequestURI());

    response.setStatus(HTTPResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ResponseModel<?> model = new ResponseModel<String>(accessDeniedException);

    response.getWriter().write(new ObjectMapper().writeValueAsString(model));
  }

}
