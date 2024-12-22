package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUserId(Long userId);
}