package org.example.bookstore.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.payload.BookDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

	private List<BookDTO> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;
	
}
