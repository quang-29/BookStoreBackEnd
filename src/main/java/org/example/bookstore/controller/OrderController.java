package org.example.bookstore.controller;

import org.example.bookstore.payload.OrderDTO;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PutMapping("/placeOrder")
    public ResponseEntity<DataResponse> placeOrder(@RequestParam UUID userId,
                                                   @RequestParam UUID cartId,
                                                   @RequestParam String paymentMethod,
                                                   @RequestParam String deliveryMethod){

        OrderDTO orderDTO = orderService.placeOrder(userId, cartId, paymentMethod, deliveryMethod);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(orderDTO)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("getOrderByUserId/{userId}")
    @PreAuthorize("#userId.toString() == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getOrderByUserId(@PathVariable UUID userId) {
        List<OrderDTO> orderDTOList = orderService.getOrdersByUser(userId);
        DataResponse dataResponse = DataResponse.builder()
                .data(orderDTOList)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
    @GetMapping("getOrderByOrderId/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getOrderByOrderId(@PathVariable UUID orderId) {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        DataResponse dataResponse = DataResponse.builder()
                .data(orderDTO)
                .code(HttpStatus.OK.value())
                .message("Success")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
}
