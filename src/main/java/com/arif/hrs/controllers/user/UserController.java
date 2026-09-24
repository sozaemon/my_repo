package com.arif.hrs.controllers.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.User;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserServiceDomain userService;
  private final UserMapper mapper;

  @GetMapping("")
  public ResponseEntity<ResponseModel<?>> getUserData(@AuthenticationPrincipal AuthUser user) {
    String userName = user.getUsername();

    Optional<UserDto> getUser = userService.findUserByUserName(userName);

    if (getUser.isEmpty()) {
      log.info("fail to fetch user");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>("unauthorized", "user is not registered"));
    }

    return ResponseEntity.ok(new ResponseModel<User>(mapper.dtoToHttp(getUser.get()), "success to fetch user"));
  }
}
