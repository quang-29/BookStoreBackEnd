package org.example.bookstore.controller;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.request.UserRequest;
import org.example.bookstore.payload.request.UserUpdate;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.payload.response.UserResponse;
import org.example.bookstore.service.Interface.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
        DataResponse dataResponse = DataResponse.builder()
                .data(userResponse)
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<DataResponse> getUserInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDTO = userService.getMyProfile(authentication.getName());
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now())
                .data(userDTO)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @GetMapping("/getUserById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getUserById(@PathVariable UUID id) {
        UserDTO userDTO = userService.getUserById(id);
        DataResponse dataResponse = DataResponse.builder()
                .data(userDTO)
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<DataResponse> updateUser(@RequestBody UserUpdate userUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        UUID currentUserId = userService.getCurrentUserId(authentication);
        if (!isAdmin) {
            if(!currentUserId.equals(userUpdate.getId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
            }
        }
        UserDTO userDTO = userService.updateUser(userUpdate);
        DataResponse dataResponse = DataResponse.builder()
                .data(userDTO)
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> deleteUser(@RequestParam UUID userId) {
        String result = userService.deleteUser(userId);
        DataResponse dataResponse = DataResponse.builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(result)
                .timestamp(LocalDateTime.now())
                .data("")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PutMapping("/likeBook")
    public ResponseEntity<DataResponse> likedBook(@RequestParam UUID userId,
                                                   @RequestParam UUID bookId) {
        String result = userService.likedBooks(userId, bookId);
        DataResponse dataResponse = DataResponse.builder()
                .data("")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(result)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @PutMapping("/removeLikedBooks")
    public ResponseEntity<DataResponse> removeLikedBooks(@RequestParam UUID userId,
                                                   @RequestParam UUID bookId) {
        String result = userService.removeLikedBooks(userId, bookId);
        DataResponse dataResponse = DataResponse.builder()
                .data("")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(result)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @GetMapping("/listBooksLikedByUser")
    public ResponseEntity<DataResponse> listBooksLikedByUser(@RequestParam UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        UUID currentUserId = userService.getCurrentUserId(authentication);
        if (!isAdmin) {
            if(!currentUserId.equals(userId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
            }
        }

        Set<BookDTO> bookDTOS = userService.listBooksLikedByUser(userId);
        DataResponse dataResponse = DataResponse.builder()
                .data(bookDTOS)
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }




}
