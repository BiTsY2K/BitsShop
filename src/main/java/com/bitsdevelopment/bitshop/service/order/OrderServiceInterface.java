package com.bitsdevelopment.bitshop.service.order;

import java.util.List;

import com.bitsdevelopment.bitshop.dto.OrderDTO;
import com.bitsdevelopment.bitshop.model.Order;

public interface OrderServiceInterface {
  Order placeOrder(Long userId);
  OrderDTO getOrder(Long orderId);
  OrderDTO convertToDTO(Order order);
  List<OrderDTO> getUserOrders(Long userId);
}
