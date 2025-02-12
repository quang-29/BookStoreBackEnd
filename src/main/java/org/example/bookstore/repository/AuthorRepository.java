package org.example.bookstore.repository;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByName(String name);

    Optional<Author> findByName(@Size(min = 3, max = 20, message = "Author Name must be between 5 and 20 characters long") @Pattern(regexp = "^[a-zA-Z]*$", message = "Author Name must not contain numbers or special characters") String name);
}
