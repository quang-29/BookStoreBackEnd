package org.example.bookstore.enums;

public enum OrderStatus {
    WAIT_PAYMENT(0),
    PAID(1),
    PROCESSING(2),
    SHIPPED(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + value);
    }
}
