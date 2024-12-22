package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  void deleteAllByCartId(Long id);
}
