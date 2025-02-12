package org.example.bookstore.repository;

import org.example.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query(value = "SELECT * FROM book WHERE book_title COLLATE utf8mb4_bin LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    Book findByName(String title);

    Page<Book> findByCategory_Name(String category, Pageable pageable);
    Page<Book> findByAuthor_Name(String authorName, Pageable pageable);


}
