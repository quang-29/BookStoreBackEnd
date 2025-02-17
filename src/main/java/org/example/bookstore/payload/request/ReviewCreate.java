package org.example.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreate {

    @JsonProperty("user")
    private UUID userId;

    @JsonProperty("book")
    private UUID bookId;

    private String content;
    private int rating;

}
