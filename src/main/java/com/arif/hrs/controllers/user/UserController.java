package com.arif.hrs.controllers.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arif.hrs.auth.model.AuthUser;
import com.arif.hrs.controllers.ResponseModel;
import com.arif.hrs.controllers.httpmodel.FUser;
import com.arif.hrs.controllers.httpmodel.PageResponse;
import com.arif.hrs.controllers.httpmodel.PaginationRequest;
import com.arif.hrs.controllers.httpmodel.UpdatePassword;
import com.arif.hrs.controllers.httpmodel.User;
import com.arif.hrs.controllers.util.ControllerUtil;
import com.arif.hrs.domain.dto.FUserDto;
import com.arif.hrs.domain.dto.PageDto;
import com.arif.hrs.domain.dto.PaginationDto;
import com.arif.hrs.domain.dto.RoleDto;
import com.arif.hrs.domain.dto.UserDto;
import com.arif.hrs.domain.service.RoleServiceDomain;
import com.arif.hrs.domain.service.UserAuthServiceDomain;
import com.arif.hrs.domain.service.UserServiceDomain;
import com.arif.hrs.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

  private final UserAuthServiceDomain userAuthService;
  private final UserServiceDomain userService;
  private final UserMapper mapper;
   private final RoleServiceDomain roleService;

  public UserController(UserAuthServiceDomain userAuthServiceDomain, UserServiceDomain userServiceDomain,
      UserMapper mapper, RoleServiceDomain roleService) {
    this.userAuthService = userAuthServiceDomain;
    this.userService = userServiceDomain;
    this.mapper = mapper;
    this.roleService =roleService;
  }

  @GetMapping("")
  public ResponseEntity<ResponseModel<?>> getUserData(@AuthenticationPrincipal AuthUser user) {
    String userName = user.getUsername();

    Optional<UserDto> getUser = userAuthService.findUserByUserName(userName);

    if (getUser.isEmpty()) {
      log.info("fail to fetch user");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ResponseModel<String>("unauthorized", "user is not registered"));
    }

    return ResponseEntity.ok(new ResponseModel<User>(mapper.dtoToHttp(getUser.get()), "success to fetch user"));
  }

  @PostMapping("/list")
  public ResponseEntity<ResponseModel<PageResponse<?>>> listUser(@RequestBody PaginationRequest request) {
    PageDto<UserDto> result = userService.paginate(new PaginationDto(request));

    List<User> user = result.getData().stream().map(mapper::dtoToHttp).toList();
    PageResponse<User> response = new PageResponse<>(result, user);

    return ControllerUtil.createSuccessResponse(response, "success to fetch user data");
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseModel<User>> createUser(@RequestBody FUser request) {
    FUserDto dto = new FUserDto();
    dto.setUserName(request.getUserName());
    dto.setEmail(request.getEmail());
    dto.setFullName(request.getFullName());
    dto.setPlainPassword(request.getPlainPassword());
    dto.setExpiryDate(request.getExpiryDate());
    UserDto result = userService.createUser(dto);

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(result), "success create user");
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseModel<User>> updateUser(@RequestBody User request) {
    UserDto result = userService.update(mapper.httpToDto(request));

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(result), "Success update User");
  }

  @DeleteMapping("/delete/{userId}")
  public ResponseEntity<ResponseModel<String>> deleteUser(@PathVariable("userId") Integer userId) {
    userService.delete(userId);

    return ControllerUtil.createSuccessResponse("Success", "Success to delete User");
  }

  @PatchMapping("/update-password")
  public ResponseEntity<ResponseModel<User>> updatePassword(
    @AuthenticationPrincipal AuthUser user,
    @RequestBody UpdatePassword request) {

    log.info("Auth Controller {}:{}", "POST", "Update password");
    String username = user.getUsername();
    UserDto result = userService.updatePassword(username, request.getOldPassword(),
        request.getNewPAssword());

    return ControllerUtil.createSuccessResponse(mapper.dtoToHttp(result), "Success update password");
  }

  @GetMapping("/user-roles")
  public ResponseEntity<ResponseModel<String>> getUserRoles(@AuthenticationPrincipal AuthUser user) {
    log.info("Auth Controller {}:{}", "GET", "fetch User Roles");
    String result = "";
    Optional<String> userRole = user.getAuthorities().stream().map(m -> m.getAuthority()).findFirst();

    if (userRole.isPresent()) {
      Optional<RoleDto> roleDto = roleService.findByName(userRole.get());

      if (roleDto.isPresent()) {
        result = roleDto.get().getDescription();
      }
    }
    return ResponseEntity.ok(new ResponseModel<String>(result, "success to fetch user roles"));
  }

}
