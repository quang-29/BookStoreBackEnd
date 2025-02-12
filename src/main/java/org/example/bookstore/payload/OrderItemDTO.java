package org.example.bookstore.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private UUID orderItemId;
    private BookDTO book;
    private Integer quantity;
    private BigDecimal productPrice;
}
