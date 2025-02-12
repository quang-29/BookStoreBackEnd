package org.example.bookstore.repository;

import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    CartItem findCartItemByCartIdAndBookId(UUID cartId, UUID bookId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    void deleteCartItemByCartIdAndBookId(UUID cartId, UUID bookId);

}
