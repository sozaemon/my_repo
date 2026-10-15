package com.arif.hrs.controllers.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.auth.constant.AuthConstant;
import com.arif.hrs.auth.jwt.AuthJwtService;
import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.LoginRequest;
import com.arif.hrs.controllers.httpmodel.Role;
import com.arif.hrs.controllers.httpmodel.TokenResponse;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.domain.service.UserAuthServiceDomain;
import com.arif.hrs.mapper.RoleMapper;
import com.arif.hrs.util.cookie.CookieComponent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final AuthenticationManager authManager;
  private final UserAuthServiceDomain userAuthService;
  private final AuthJwtService jwtService;
  private final CookieComponent cookieComponent;
 

  private final RoleMapper roleMapper;

  public AuthController(
      AuthenticationManager authManager,
      UserAuthServiceDomain userAuthService,
      AuthJwtService jwtService,
      CookieComponent cookieComponent,
      RoleMapper roleMapper) {
    this.authManager = authManager;
    this.userAuthService = userAuthService;
    this.jwtService = jwtService;
    this.cookieComponent = cookieComponent;
    this.roleMapper = roleMapper;
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseModel<?>> createJwtToken(@RequestBody LoginRequest request,
      HttpServletResponse response) {

    log.info("AuthController {}", "create jwt token");

    boolean isAuthenticated = authenticateLogin(request);
    if (isAuthenticated) {
      Optional<UserDto> user = userAuthService.findUserByUserName(request.getUserName());

      if (!user.isPresent()) {
        log.warn("user is not present {}", request.getUserName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
      }

      // validate if role request match with user role
      List<String> roles = userAuthService
          .findUserRole(request.getUserName())
          .stream()
          .map(RoleDto::getName).toList();

      if (!roles.contains(String.valueOf(request.getRole()))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "role request is not matched"));
      }

      UserDto userDto = user.get();
      try {

        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put(AuthConstant.CLAIMS_ROLE, request.getRole());
        additionalClaims.put(AuthConstant.CLAIMS_USERNAME, userDto.getUserName());

        final String token = jwtService.createJwtToken(userDto.getUserName(), additionalClaims);
        final String refreshToken = jwtService.createRefreshToken(userDto.getUserName(), additionalClaims);

        ResponseCookie cookie = cookieComponent.setCookie(AuthConstant.JWT_KEY, token).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        ResponseCookie refreshCookie = cookieComponent.setCookie(AuthConstant.JWT_KEY_REFRESH, refreshToken).build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        TokenResponse result = new TokenResponse(token, refreshToken);
        return ResponseEntity.ok(new ResponseModel<TokenResponse>(result, "authentication success"));

      } catch (Exception e) {
        log.warn("fail to generate token", e);
        return ResponseEntity.internalServerError()
            .body(new ResponseModel<String>(e));
      }

    }
    log.info("user is not authorized");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "login fail"));
  }

  @PostMapping(AuthConstant.REFRESH_REQUEST)
  public ResponseEntity<ResponseModel<String>> refreshToken(HttpServletResponse response,
      @AuthenticationPrincipal AuthUser user) {
    log.info("Auth Controller {}", "refresh token");

    if (user == null) {
      log.warn("fail to fetch principal");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
    }

    String userName = user.getUsername();

    Optional<UserDto> userDto = userAuthService.findUserByUserName(userName);

    if (!userDto.isPresent()) {
      log.warn("user is not present {}", userName);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
    }

    UserDto getUser = userDto.get();

    try {

      String userRole = user.getAuthorities().stream()
          .map(m -> m.getAuthority()).findFirst().orElseGet(() -> "");

      Map<String, Object> additionalClaims = new HashMap<>();
      additionalClaims.put(AuthConstant.CLAIMS_ROLE, userRole);
      additionalClaims.put(AuthConstant.CLAIMS_USERNAME, getUser.getUserName());

      final String token = jwtService.createJwtToken(getUser.getUserName(), additionalClaims);

      ResponseCookie cookie = cookieComponent.setCookie(AuthConstant.JWT_KEY, token).build();
      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    } catch (Exception e) {
      log.warn("fail to generate token", e);
      return ResponseEntity.internalServerError()
          .body(new ResponseModel<String>(e));
    }

    return ResponseEntity.ok(new ResponseModel<String>("success", "refresh token success"));

  }

  @PostMapping("/user-roles")
  public ResponseEntity<ResponseModel<?>> authenticateLoginAndFetchRole(@RequestBody LoginRequest request) {
    log.info("Auth Controller {}:{}", "POST", "fetch User Roles");
    boolean isAuthenticated = authenticateLogin(request);

    if (!isAuthenticated) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "login fail"));
    }

    List<RoleDto> roles = userAuthService.findUserRole(request.getUserName());
    List<Role> response = roles.stream().map(roleMapper::dtoToHttp).toList();

    return ResponseEntity.ok(new ResponseModel<List<Role>>(response, "success fetch user roles"));
  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseModel<String>> logOut(HttpServletResponse response) {
    log.info("Auth Controller {}", "Log Out");

    ResponseCookie cookie = cookieComponent.setCookie(AuthConstant.JWT_KEY, null).clear();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    ResponseCookie refreshCookie = cookieComponent.setCookie(AuthConstant.JWT_KEY_REFRESH, null).clear();
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    return ResponseEntity.ok(new ResponseModel<String>("log out success", "user logged out"));
  }

  private boolean authenticateLogin(LoginRequest login) {
    Authentication auth = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword()));

    return auth.isAuthenticated();
  }
}
