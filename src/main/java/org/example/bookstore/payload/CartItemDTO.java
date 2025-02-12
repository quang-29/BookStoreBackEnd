package org.example.bookstore.payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Cart;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private UUID cartItemId;
    private BookDTO book;
    private Integer quantity;
    private BigDecimal bookPrice;
}
