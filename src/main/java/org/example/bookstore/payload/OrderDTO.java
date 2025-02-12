package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private UUID orderId;
    private UserOrderDTO user;
    private List<OrderItemDTO> orderItem = new ArrayList<>();
    private LocalDate orderDate;
    private String paymentMethod;
    private String deliveryMethod;
    private BigDecimal totalPrice;
    private BigDecimal shippingPrice;
    private BigDecimal totalAmount;
    private int orderStatus;
}
