package org.example.bookstore.payload;

import lombok.*;
import org.example.bookstore.model.Cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {

    private UUID cartId;
    private List<CartItemDTO> cartItem = new ArrayList<>();
    private BigDecimal totalPrice;

}