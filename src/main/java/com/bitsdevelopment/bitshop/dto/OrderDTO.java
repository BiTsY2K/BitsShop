package com.bitsdevelopment.bitshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
  private Long id;
  private Long userId;
  private LocalDate orderDate;
  private BigDecimal totalAmount;
  private String status;
  private List<OrderItemDTO> items;
}
