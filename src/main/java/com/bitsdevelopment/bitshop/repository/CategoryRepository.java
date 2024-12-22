package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);
  boolean existsByName(String name);
}
