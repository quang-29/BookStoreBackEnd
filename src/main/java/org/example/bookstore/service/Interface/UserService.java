package org.example.bookstore.service.Interface;

import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.request.UserUpdate;
import org.example.bookstore.payload.response.UserResponse;
import org.springframework.security.core.Authentication;
import java.util.Set;
import java.util.UUID;


public interface UserService {

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getUserById(UUID userId);

    UserDTO updateUser(UserUpdate userUpdate);

    String deleteUser(UUID userId);

    UserDTO getMyProfile(String username);

    String likedBooks(UUID userId,UUID bookId);

    String removeLikedBooks(UUID userId,UUID bookId);

    Set<BookDTO> listBooksLikedByUser(UUID userId);

    UUID getCurrentUserId(Authentication authentication);
}
