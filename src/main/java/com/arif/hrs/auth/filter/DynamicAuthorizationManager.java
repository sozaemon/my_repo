package com.arif.hrs.auth.filter;

import java.util.List;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.arif.hrs.auth.constant.AuthConstant;
import com.arif.hrs.domain.dto.AccessDto;
import com.arif.hrs.domain.service.AccessServiceDomain;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AccessServiceDomain accessService;

  @Autowired
  public DynamicAuthorizationManager(AccessServiceDomain accessService) {
    this.accessService = accessService;
  }

  @Override
  public @Nullable AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication,
      RequestAuthorizationContext context) {

    log.info("validate role with request route");
    final AntPathMatcher matcher = new AntPathMatcher();

    HttpServletRequest request = context.getRequest();
    String requestUri = request.getRequestURI();
    String method = request.getMethod();

    // skip validation on white listed request
    if (requestUri.equals("/api/auth" + AuthConstant.REFRESH_REQUEST)){
      return new AuthorizationDecision(true);
    }
    
    Authentication auth = authentication.get();

    if (auth == null || !auth.isAuthenticated()) {
      return new AuthorizationDecision(false);
    }

    List<String> roles = auth.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    List<AccessDto> roleAccess = accessService.getAccessByRolesAndPathAndMethod(roles, method);

    Boolean hasAccess = roleAccess.stream()
        .map(AccessDto::getPath)
        .anyMatch(m -> matcher.match(m, requestUri));

    return new AuthorizationDecision(hasAccess);
  }
}
