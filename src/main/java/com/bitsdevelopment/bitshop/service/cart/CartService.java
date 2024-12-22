package com.bitsdevelopment.bitshop.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.repository.CartItemRepository;
import com.bitsdevelopment.bitshop.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements CartServiceInterface {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    System.out.println("Cart Items clear1: "+cart.getItems());
    cartItemRepository.deleteAllByCartId(id);
    System.out.println("Cart Items clear2: "+cart.getItems());
    cart.clearCart();
    // cart.getItems().clear();
    cartRepository.deleteById(id);
  }

  @Override
  public Cart getCart(Long id) {
    Cart cart = cartRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Cart Not Found!"));
    BigDecimal totalAmount = cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);
    return cartRepository.save(cart);
  }

  @Override
  public BigDecimal getTotalAmount(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }

  @Override
  public Cart initializeNewCart(User user) {
    return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() -> {
      Cart cart = new Cart();
      cart.setUser(user);
      return cartRepository.save(cart);
    });
  }
  
  @Override
  public Cart getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId);
  }

}
