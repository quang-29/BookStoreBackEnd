package org.example.bookstore.service.Interface;

import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getUserById(UUID userId);

    UserDTO updateUser(UUID userId, UserDTO userDTO);

    String deleteUser(UUID userId);

    List<UserResponse> getAllUser();

    UserDTO getMyProfile(String username);
}
