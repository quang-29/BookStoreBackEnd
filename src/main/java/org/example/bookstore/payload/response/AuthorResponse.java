package org.example.bookstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.model.Book;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private UUID id;
    private String name;
    private String bio;
    private String email;
    private String address;
    private String phone;
    private Set<Book> books;
}
