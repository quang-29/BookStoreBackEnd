package org.example.bookstore.repository;

import org.example.bookstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("select c from Cart c where c.user.username = ?1")
    Cart getCartByUserName(String userName);
}
