package org.example.bookstore.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTypeDTO {
    private int paymentId;
    private String paymentMethod;
}
