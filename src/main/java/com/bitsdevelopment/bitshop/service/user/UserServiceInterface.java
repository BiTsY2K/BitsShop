package com.bitsdevelopment.bitshop.service.user;

import com.bitsdevelopment.bitshop.dto.UserDTO;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.request.CreateUserRequest;
import com.bitsdevelopment.bitshop.request.UpdateUserRequest;

public interface UserServiceInterface {
  User createUser(CreateUserRequest request);
  User updateUser(UpdateUserRequest request, Long userId);
  void deleteUser(Long userId);
  
  User getAuthenticatedUser();
  User getUserById(Long userId);
  UserDTO convertUserToDTO(User user);
}
