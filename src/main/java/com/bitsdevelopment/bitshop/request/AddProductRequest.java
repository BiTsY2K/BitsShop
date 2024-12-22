package com.bitsdevelopment.bitshop.request;

import java.math.BigDecimal;

import com.bitsdevelopment.bitshop.model.Category;

import lombok.Data;

@Data
public class AddProductRequest {
  private String name;
  private String brand;
  private BigDecimal price;
  private String description;
  private int inventory;
  private Category category;
}
