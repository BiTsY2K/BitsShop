package com.bitsdevelopment.bitshop.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitsdevelopment.bitshop.dto.OrderDTO;
import com.bitsdevelopment.bitshop.enums.OrderStatus;
import com.bitsdevelopment.bitshop.exceptions.ResourceNotFoundException;
import com.bitsdevelopment.bitshop.model.Cart;
import com.bitsdevelopment.bitshop.model.Order;
import com.bitsdevelopment.bitshop.model.OrderItem;
import com.bitsdevelopment.bitshop.model.Product;
import com.bitsdevelopment.bitshop.repository.OrderRepository;
import com.bitsdevelopment.bitshop.repository.ProductRepository;
import com.bitsdevelopment.bitshop.service.cart.CartServiceInterface;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {
  private final ModelMapper modelMapper;
  private final ProductRepository productRepository;
  private final CartServiceInterface cartService;
  private final OrderRepository orderRepository;

  @Transactional
  @Override
  public Order placeOrder(Long userId) {
    Cart cart = cartService.getCartByUserId(userId);
    Order order = createOrder(cart);
    List<OrderItem> orderItems = createOrderItems(order, cart);
    order.setOrderItems(new HashSet<>(orderItems));
    order.setTotalAmount(calculateTotalAmount(orderItems));
    cartService.clearCart(cart.getId());
    return orderRepository.save(order);
  }

  private Order createOrder(Cart cart) {
    Order newOrder = new Order();
    newOrder.setOrderStatus(OrderStatus.PENDING);
    newOrder.setOrderDate(LocalDate.now());
    newOrder.setUser(cart.getUser());
    return newOrder;
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return (cart.getItems().stream().map(cartItem -> {
      Product product = cartItem.getProduct();
      product.setInventory(product.getInventory() - cartItem.getQuantity());
      productRepository.save(product);
      return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
    })).toList();
  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
    return orderItemList.stream().map(item -> item.getPrice()
        .multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public OrderDTO getOrder(Long orderId) {
    return orderRepository.findById(orderId).map(this::convertToDTO)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
  }

  @Override
  public OrderDTO convertToDTO(Order order) {
    return modelMapper.map(order, OrderDTO.class);
  }

  @Override
  public List<OrderDTO> getUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream().map(this::convertToDTO).toList();
  }
}
