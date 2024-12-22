package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.cart.CartItemServiceInterface;
import com.bitsdevelopment.bitshop.service.cart.CartServiceInterface;
import com.bitsdevelopment.bitshop.service.user.UserServiceInterface;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final UserServiceInterface userService;
  private final CartServiceInterface cartService;
  private final CartItemServiceInterface cartItemService;

  @PostMapping("/item/add")
  public ResponseEntity<APIResponse> addItemToCart(@RequestParam(required = false) Long cartId, @RequestParam Long productId, @RequestParam Integer quantity) {
    try {
      User user = userService.getAuthenticatedUser();
      Cart cart = cartService.initializeNewCart(user);
      cartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Your item has been added to the cart!", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/item/remove/cart/{cartId}/item/{itemId}")
  public ResponseEntity<APIResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
    try {
      cartItemService.removeItemFromCart(cartId, itemId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Item removed, Your cart has been updated.", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/item/update/cart/{cartId}/item/{itemId}")
  public ResponseEntity<APIResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam Integer quantity) {
    try {
      cartItemService.updateItemQuantity(cartId, itemId, quantity);
      return ResponseEntity.ok(new APIResponse("SUCCESS! The item in your cart has been updated.", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
    }
  }

}
