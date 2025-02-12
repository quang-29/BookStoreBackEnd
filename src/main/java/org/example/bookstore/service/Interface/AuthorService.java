package org.example.bookstore.service.Interface;


import org.example.bookstore.payload.AuthorDTO;
import org.springframework.stereotype.Service;

import java.lang.Long;
import java.util.List;
import java.util.UUID;


public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO updateAuthor(UUID id, AuthorDTO authorDTO);
    boolean deleteAuthor(UUID id);
    AuthorDTO getAuthorById(UUID id);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO getAuthorByName(String authorName);
}
