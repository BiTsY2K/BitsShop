package com.bitsdevelopment.bitshop.request;

import lombok.Data;

import java.math.BigDecimal;

import com.bitsdevelopment.bitshop.model.Category;

@Data
public class UpdateProductRequest {
  private String name;
  private String brand;
  private BigDecimal price;
  private String description;
  private int inventory;
  private Category category;
}
