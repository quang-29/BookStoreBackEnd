package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.APIException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.OrderItemDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.UserOrderDTO;
import org.example.bookstore.repository.*;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.service.Interface.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    private enum orderStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        CANCELED
    }
    @Override
    public OrderDTO placeOrder(UUID userId, UUID cartId, String paymentMethod, String deliveryMethod) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.size() == 0) {
            throw new AppException(ErrorCode.ORDER_ERROR);
        }

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setUser(user);
        order.setEmail(user.getEmail());
        PaymentType paymentType = paymentRepository.findByPaymentMethod(paymentMethod);
        if(paymentType == null) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND);
        }
        DeliveryType deliveryType = deliveryRepository.findByName(deliveryMethod);
        if(deliveryType == null) {
            throw new AppException(ErrorCode.ORDER_ERROR);
        }
        order.setPaymentMethod(paymentType);
        order.setDeliveryMethod(deliveryType);
        order.setDeliveryAt(null);
        order.setPaidAt(null);
        order.setTotalPrice(cart.getTotalPrice());
        order.setTotalAmount(cart.getTotalPrice().add(deliveryType.getPrice()));
        order.setOrderStatus(orderStatus.PENDING.ordinal());
        order.setShippingPrice(deliveryType.getPrice());
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(cartItem.getBookPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        for (int i = 0; i < cart.getCartItems().size(); i++) {
            CartItem cartItem1 = cart.getCartItems().get(i);
            int quantity = cartItem1.getQuantity();
            Book book = cartItem1.getBook();
            cartService.deleteProductFromCart(cartId, book.getId());
            book.setStock(book.getStock() - quantity);
            book.setSold(book.getSold() + quantity);
            bookRepository.save(book);
        }
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderDTO.setUser(modelMapper.map(user, UserOrderDTO.class));
        orderItems.forEach(item -> orderDTO.getOrderItem().add(modelMapper.map(item, OrderItemDTO.class)));
        return orderDTO;

    }

    @Override
    public OrderDTO getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setOrderItem(order.getOrderItems().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class)).collect(Collectors.toList()));
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(UUID userId) {

        List<Order> orders = orderRepository.findAllByUserId(userId);
        if (orders.size() == 0) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
                    orderDTO.setOrderItem(order.getOrderItems().stream()
                            .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                            .collect(Collectors.toList()));
                    return orderDTO;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.size() == 0) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
                    orderDTO.setOrderItem(order.getOrderItems().stream()
                            .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                            .collect(Collectors.toList()));
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(UUID orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(Integer.parseInt(orderStatus));
        return modelMapper.map(orderRepository.save(order), OrderDTO.class);
    }

    @Override
    public String cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getOrderStatus() == orderStatus.PROCESSING.ordinal() ) {
            throw new APIException("This order cannot be canceled because it is already being processed or completed.");
        }

        order.setOrderStatus(orderStatus.CANCELED.ordinal());
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);
        for (OrderItem orderItem : orderItems) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());
             bookRepository.save(book); // Cần thêm bookRepository
        }
        return "Order with id " + orderId + " has been canceled successfully.";
    }
}
