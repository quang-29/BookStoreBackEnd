package org.example.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<OrderItem> orderItems;

    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentType paymentMethod;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryType deliveryMethod;

    private BigDecimal totalPrice;

    @Column(name = "shipping_price")
    private BigDecimal shippingPrice;

    private BigDecimal totalAmount;
    @Column(name = "isPaid", nullable = false)
    private int isPaid = 0;

    @Column(name = "isDelivered", nullable = false)
    private int isDelivered = 0;

    @Column(name = "paidAt")
    private Date paidAt = new Date();

    @Column(name = "deliveryAt")
    private Date deliveryAt = new Date();

    @Column(name = "status")
    private int orderStatus = 0;
}
