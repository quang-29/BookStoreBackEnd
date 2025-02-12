package org.example.bookstore.controller;


import org.example.bookstore.model.Author;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    private static final String AUTHOR_CREATE = "Author created successfully";
    private static final String AUTHOR_UPDATE = "Author updated successfully";
    private static final String AUTHOR_DELETE = "Author deleted successfully";
    private static final String AUTHOR_INFO = "Get author information successfully";



    @Autowired
    private AuthorService authorService;

    @PostMapping("/addAuthor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> addAuthor(@RequestBody AuthorDTO authorDTO) {

        AuthorDTO author = authorService.createAuthor(authorDTO);
        DataResponse dataResponse = DataResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .data(author)
                .message(AUTHOR_CREATE)
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.CREATED);
    }
    @PutMapping("/updateAuthor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> editAuthor(@PathVariable UUID id, @RequestBody AuthorDTO newAuthorDTO) {
        AuthorDTO author = authorService.updateAuthor(id, newAuthorDTO);
        DataResponse dataResponse = DataResponse
                .builder()
                .code(HttpStatus.OK.value())
                .data(author)
                .message(AUTHOR_UPDATE)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAuthor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> deleteAuthor(@PathVariable UUID id) {
        boolean result = authorService.deleteAuthor(id);
        DataResponse dataResponse = DataResponse
                .builder()
                .code(HttpStatus.OK.value())
                .data(result)
                .message(AUTHOR_DELETE)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/getAuthorById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> getAuthorById(@PathVariable UUID id) {
        AuthorDTO authorDTO = authorService.getAuthorById(id);
        DataResponse dataResponse = DataResponse
                .builder()
                .code(HttpStatus.OK.value())
                .data(authorDTO)
                .message(AUTHOR_INFO)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);

    }

    @GetMapping("/getAllAuthors")
    public ResponseEntity<DataResponse> getAllAuthors() {
        List<AuthorDTO> authorDTOS = authorService.getAllAuthors();
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(authorDTOS)
                .message(AUTHOR_INFO)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/getAuthorByName/{authorName}")
    public ResponseEntity<DataResponse> getAuthorByName(@RequestParam String authorName) {
        AuthorDTO authorDTO = authorService.getAuthorByName(authorName);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(authorDTO)
                .message(AUTHOR_INFO)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }
}
