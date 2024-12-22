package com.bitsdevelopment.bitshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import com.bitsdevelopment.bitshop.model.Category;

@Data
public class ProductDTO {
  private Long id;
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;
  private Category category;
  private List<ImageDTO> images;
}
