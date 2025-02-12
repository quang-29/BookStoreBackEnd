package org.example.bookstore.payload;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private UUID id;
    private String categoryName;
//    private List<BookDTO> bookDTOList;
}
