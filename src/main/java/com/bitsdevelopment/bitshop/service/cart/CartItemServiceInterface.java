package com.bitsdevelopment.bitshop.service.cart;

public interface CartItemServiceInterface {
  void addItemToCart(Long cartId, Long productId, int quantity);
  void removeItemFromCart(Long cartId, Long productId);
  void updateItemQuantity(Long cartId, Long productId, int quantity);
}
