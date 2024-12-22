package com.bitsdevelopment.bitshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByProductId(Long id);
}
