package com.bitsdevelopment.bitshop.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.model.CartItem;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.repository.CartItemRepository;
import com.bitsdevelopment.bitshop.repository.CartRepository;
import com.bitsdevelopment.bitshop.service.product.ProductServiceInterface;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements CartItemServiceInterface {
  private final ProductServiceInterface productService;
  private final CartServiceInterface cartService;
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;

  @Override
  public void addItemToCart(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);
    Product product = productService.getProductById(productId);
    CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .orElse(new CartItem());

    if (cartItem.getId() != null) {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    } else {
      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setUnitPrice(product.getPrice());
    }

    cartItem.setTotalPrice();
    cart.addItem(cartItem);
    cartItemRepository.save(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    Cart cart = cartService.getCart(cartId);
    CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Product Not Found."));
    cart.removeItem(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);
    cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().ifPresent(item -> {
      item.setQuantity(quantity);
      item.setUnitPrice(item.getProduct().getPrice());
      item.setTotalPrice();
    });
    BigDecimal totalAmount = cart.getItems().stream()
        .map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    cart.setTotalAmount(totalAmount);
    cartRepository.save(cart);
  }

}
