package com.arif.hrs.controllers.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.arif.hrs.controllers.httpmodel.TokenResponse;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.util.cookie.CookieComponent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthenticationManager authManager;
  private final UserServiceDomain userService;
  private final AuthJwtService jwtService;
  private final CookieComponent cookieComponent;

  @PostMapping("/login")
  public ResponseEntity<ResponseModel<?>> createJwtToken(@RequestBody LoginRequest request,
      HttpServletResponse response) {

    log.info("AuthController {}", "create jwt token");
    Authentication auth = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

    if (auth.isAuthenticated()) {
      Optional<UserDto> user = userService.findUserByUserName(request.getUserName());

      if (!user.isPresent()) {

        log.warn("user is not present {}", request.getUserName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
      }

      UserDto userDto = user.get();

      try {

        final String token = jwtService.createJwtToken(userDto.getUserName(), userDto.getEmail());
        final String refreshToken = jwtService.createRefreshToken(userDto.getUserName(), userDto.getEmail());

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
    log.info("refresh token");

    if (user == null) {
      log.warn("fail to fetch principal");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
    }

    String userName = user.getUsername();

    Optional<UserDto> userDto = userService.findUserByUserName(userName);

    if (!userDto.isPresent()) {
      log.warn("user is not present {}", userName);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "user is not registered"));
    }

    UserDto getUser = userDto.get();

    try {

      final String token = jwtService.createJwtToken(getUser.getUserName(), getUser.getEmail());
      ResponseCookie cookie = cookieComponent.setCookie(AuthConstant.JWT_KEY, token).build();
      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    } catch (Exception e) {
      log.warn("fail to generate token", e);
      return ResponseEntity.internalServerError()
          .body(new ResponseModel<String>(e));
    }

    return ResponseEntity.ok(new ResponseModel<String>("success", "refresh token success"));

  }

  @PostMapping("/logout")
  public ResponseEntity<ResponseModel<String>> logOut(HttpServletResponse response) {
    log.info("log out");

    ResponseCookie cookie = cookieComponent.setCookie(AuthConstant.JWT_KEY, null).clear();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    ResponseCookie refreshCookie = cookieComponent.setCookie(AuthConstant.JWT_KEY_REFRESH, null).clear();
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    return ResponseEntity.ok(new ResponseModel<String>("log out success", "user logged out"));
  }

  @GetMapping("/user-roles")
  public ResponseEntity<ResponseModel<String[]>> getUserRoles(@AuthenticationPrincipal AuthUser user) {

    String[] userRoles = user.getAuthorities().stream().map(m -> m.getAuthority()).toArray(String[]::new);
    return ResponseEntity.ok(new ResponseModel<String[]>(userRoles, "success to fetch user roles"));
  }

}
