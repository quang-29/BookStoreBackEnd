package org.example.bookstore.repository;

import org.example.bookstore.model.Book;
import org.example.bookstore.model.Review;
import org.example.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByBookAndUser(Book book, User user);


    @Query("select r from Review r where r.user.id = ?1")
    List<Review> findAllReviewsByUserId(UUID userId);
}
