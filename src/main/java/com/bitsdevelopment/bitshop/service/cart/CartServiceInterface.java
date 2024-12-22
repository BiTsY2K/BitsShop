package com.bitsdevelopment.bitshop.service.cart;

import java.math.BigDecimal;

import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.model.User;

public interface CartServiceInterface {
  void clearCart(Long id);
  Cart getCart(Long id);
  BigDecimal getTotalAmount(Long id);

  Cart initializeNewCart(User user);
  Cart getCartByUserId(Long userId);
}
