package org.example.bookstore.controller;

import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.request.UserRequest;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.payload.response.UserResponse;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user/registerUser")
    public String registerUser(@RequestBody UserRequest userRequest) {

        return "success";
    }

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

}
