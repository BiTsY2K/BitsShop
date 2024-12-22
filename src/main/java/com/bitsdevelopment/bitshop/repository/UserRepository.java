package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
  User findByEmail(String email);
}
