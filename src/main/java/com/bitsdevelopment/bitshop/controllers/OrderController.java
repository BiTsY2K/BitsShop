package com.bitsdevelopment.bitshop.controllers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bitsdevelopment.bitshop.dto.OrderDTO;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Order;
import com.bitsdevelopment.bitshop.response.APIResponse;
import com.bitsdevelopment.bitshop.service.order.OrderServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final OrderServiceInterface orderService;

  @PostMapping("/order")
  public ResponseEntity<APIResponse> createOrder(@RequestParam Long userId) {
    try {
      Order order = orderService.placeOrder(userId);
      OrderDTO orderDto = orderService.convertToDTO(order);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Order placed!", orderDto));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("ERROR", e.getMessage()));
    }
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<APIResponse> getOrderById(@PathVariable Long orderId) {
    try {
      OrderDTO order = orderService.getOrder(orderId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Here is Your Order.", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse("ERROR", e.getMessage()));
    }
  }

  @GetMapping("/user/{userId}/orders")
  public ResponseEntity<APIResponse> getUserOrders(@PathVariable Long userId) {
    try {
      List<OrderDTO> order = orderService.getUserOrders(userId);
      return ResponseEntity.ok(new APIResponse("SUCCESS! Here are all your order history.", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new APIResponse("ERROR", e.getMessage()));
    }
  }
}
