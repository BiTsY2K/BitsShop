package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryName(String category);
  List<Product> findByBrand(String brand);
  List<Product> findByCategoryNameAndBrand(String category, String brand);
  List<Product> findByName(String name);
  List<Product> findByBrandAndName(String brand, String name);
  Long countByBrandAndName(String brand, String name);
  boolean existsByBrandAndName(String brand, String name);
}