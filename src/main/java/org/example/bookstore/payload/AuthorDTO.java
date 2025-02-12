package org.example.bookstore.payload;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bookstore.model.Book;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDTO {
    private UUID id;
    private String authorName;
    private String bio;
    private String email;
    private String address;
    private String phone;
//    private Set<Book> books;
}
