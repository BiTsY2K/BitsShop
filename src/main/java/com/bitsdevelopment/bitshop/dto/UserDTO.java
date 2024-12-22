package com.bitsdevelopment.bitshop.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private List<OrderDTO> orders;
  private CartDTO cart;
}
