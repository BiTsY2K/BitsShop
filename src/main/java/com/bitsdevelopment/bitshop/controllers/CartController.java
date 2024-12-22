package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.cart.CartServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
  private final CartServiceInterface cartService;

  @DeleteMapping("/cart/{cartId}/clear")
  public ResponseEntity<APIResponse> clearCart(@PathVariable Long cartId) {
    try {
      cartService.clearCart(cartId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! All items have been removed from your cart.", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/cart/{cartId}")
  public ResponseEntity<APIResponse> getCart(@PathVariable Long cartId) {
    try {
      Cart cart = cartService.getCart(cartId);
      return ResponseEntity.ok(new APIResponse("SUCCESS!", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/cart/{cartId}/total")
  public ResponseEntity<APIResponse> getTotalAmount(@PathVariable Long cartId) {
    try {
      BigDecimal totalAmount = cartService.getTotalAmount(cartId);
      return ResponseEntity.ok(new APIResponse("SUCCESS!", totalAmount));
    } catch (ResourceNotFoundException e) { 
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }
}
