package org.example.bookstore.service.Interface;

import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface OrderService {

    OrderDTO placeOrder(UUID userId, UUID cartId, String paymentMethod, String deliveryMethod);

    OrderDTO getOrder(UUID orderId);

    List<OrderDTO> getOrdersByUser(UUID userId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(UUID orderId, String orderStatus);

    String cancelOrder(UUID userId, UUID orderId);
}
