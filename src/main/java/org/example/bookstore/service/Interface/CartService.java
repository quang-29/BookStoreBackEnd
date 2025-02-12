package org.example.bookstore.service.Interface;

import org.example.bookstore.payload.CartDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


public interface CartService {
    CartDTO addProductToCart(UUID cartId, UUID bookId, Integer quantity);

    List<CartDTO> getAllCarts();

    @Query("SELECT c FROM Cart c WHERE c.user.username = :userName")
    CartDTO getCartByUserName(String userName);


    boolean deleteProductFromCart(UUID cartId, UUID bookId);
}
