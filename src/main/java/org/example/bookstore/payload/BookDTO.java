package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String publisher;
    private String isbn;
    private String language;
    private String imagePath;
    private Long stock;
    private Long sold;
    @JsonProperty("category")
    private String categoryName;
    @JsonProperty("author")
    private String authorName;

}