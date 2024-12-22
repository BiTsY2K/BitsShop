package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.request.LoginRequest;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.response.JwtResponse;
import com.bitsdevelopment.bitshop.security.jwt.JwtUtils;
import com.bitsdevelopment.bitshop.security.user.ShopUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<APIResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateTokenForUser(authentication);
      ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
      JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
      return ResponseEntity.ok(new APIResponse("Congratulation! You Logged In Successfully.", jwtResponse));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(e.getMessage(), null));
    }
  }
}
