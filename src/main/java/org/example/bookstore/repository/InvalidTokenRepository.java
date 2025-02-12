package org.example.bookstore.repository;

import org.example.bookstore.model.TokenInvalid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvalidTokenRepository extends JpaRepository<TokenInvalid, UUID> {
    boolean existsByToken(String token);

    boolean existsById(UUID uuid);
}
