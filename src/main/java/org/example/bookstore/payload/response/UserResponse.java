package org.example.bookstore.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.payload.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	
	private List<UserDTO> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;
	
}
