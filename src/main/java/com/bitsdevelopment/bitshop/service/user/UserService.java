package com.bitsdevelopment.bitshop.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bitsdevelopment.bitshop.dto.UserDTO;
import com.bitsdevelopment.bitshop.exceptions.AlreadyExistsException;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.repository.UserRepository;
import com.bitsdevelopment.bitshop.request.CreateUserRequest;
import com.bitsdevelopment.bitshop.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  
  @Override
  public User createUser(CreateUserRequest request) {
    return Optional.of(request).filter(user -> !userRepository.existsByEmail(user.getEmail()))
      .map(req -> {
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(newUser);
      }).orElseThrow(() -> new AlreadyExistsException("User with email "+request.getEmail()+" already exists!"));
  }

  @Override
  public User updateUser(UpdateUserRequest request, Long userId) {
    return userRepository.findById(userId).map(user -> {
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      return userRepository.save(user);
    }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

  }

  @Override
  public void deleteUser(Long userId) {
    userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
      throw new ResourceNotFoundException("User not found!");
    });
  }

  @Override
  public User getAuthenticatedUser() {
    Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userRepository.findByEmail(email);
  }

  @Override
  public User getUserById(Long userId) {
    return userRepository.findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
  }

  @Override
  public UserDTO convertUserToDTO(User user) {
    return modelMapper.map(user, UserDTO.class);
  }

}
