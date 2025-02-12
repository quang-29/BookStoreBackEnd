package org.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "book_title")
    private String title;

    @Column(name = "book_description")
    private String description;

    @Column(name = "book_page")
    private int page;

    @Column(name = "reprint")
    private int reprint;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "sold", nullable = false)
    private Long sold;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "ISBN", nullable = false)
    private String isbn;

    @Column(name = "language")
    private String language;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<RatingBook> ratingBooks;

    @OneToMany(mappedBy = "book")
    private List<OrderItem> orderDetails;




}