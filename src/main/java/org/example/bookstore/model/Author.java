package org.example.bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(min = 3, max = 20, message = "Author Name must be between 5 and 20 characters long")
    @Column(name = "author_name")
    private String name;

    @Column(name = "bio")
    private String bio;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    @Column(name = "phone_number")
    private String phone;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

}