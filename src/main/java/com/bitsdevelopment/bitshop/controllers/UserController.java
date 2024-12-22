package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.dto.UserDTO;
import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.request.CreateUserRequest;
import com.bitsdevelopment.bitshop.request.UpdateUserRequest;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.user.UserServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final UserServiceInterface userService;

  @GetMapping("/user/{userId}")
  public ResponseEntity<APIResponse> getUserById(@PathVariable Long userId) {
    try {
      User user = userService.getUserById(userId);
      UserDTO userDto = userService.convertUserToDTO(user);
      return ResponseEntity.ok(new APIResponse("SUCCESS", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/user/add")
  public ResponseEntity<APIResponse> createUser(@RequestBody CreateUserRequest request) {
    try {
      User user = userService.createUser(request);
      UserDTO userDto = userService.convertUserToDTO(user);
      return ResponseEntity.ok(new APIResponse("SUCCESS! A new user account has been created.", userDto));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/user/{userId}/update")
  public ResponseEntity<APIResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
    try {
      User user = userService.updateUser(request, userId);
      UserDTO userDto = userService.convertUserToDTO(user);
      return ResponseEntity.ok(new APIResponse("SUCCESS! User details have been updated.", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/user/{userId}/delete")
  public ResponseEntity<APIResponse> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! User account has been deleted.", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }
}
