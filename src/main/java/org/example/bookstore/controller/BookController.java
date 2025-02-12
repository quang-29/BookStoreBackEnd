package org.example.bookstore.controller;

import org.example.bookstore.model.Book;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.payload.response.BookResponse;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.payload.response.CreateBookResponse;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.AwsS3Service;
import org.example.bookstore.service.Interface.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private static  final String ADD_BOOK = "Add Book successfully";
    private static  final String GET_BOOK = "GET Book information successfully";
    private static  final String UPLOAD_IMAGE_BOOK = "Upload Image Book successfully";


    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getBookById(@PathVariable UUID id) {
        BookDTO bookDTO = bookService.getBookById(id);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .data(bookDTO)
                .message(GET_BOOK)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> addBook(@RequestBody CreateBookRequest request) {
        BookDTO bookDTO = bookService.addBook(request);
        DataResponse response = DataResponse.builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message(ADD_BOOK)
                .data(bookDTO)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/uploadImageBook/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> uploadImageBook(
            @PathVariable UUID bookId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryResponse cloudinaryResponse= bookService.uploadImageBook(bookId,file);
        DataResponse response = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message(UPLOAD_IMAGE_BOOK)
                .data(cloudinaryResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/uploadImageB/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> uploadImageB(
            @PathVariable UUID bookId,
            @RequestParam("file") MultipartFile file) {
        String imageUrl = bookService.uploadImageB(bookId,file);
        DataResponse response = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message(UPLOAD_IMAGE_BOOK)
                .data(imageUrl)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }



    @GetMapping("/getAllBooks")
    public ResponseEntity<BookResponse> getAllBooks(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        BookResponse books = bookService.getAllBooks(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/getAllBookByAuthor/{authorName}")
    public ResponseEntity<BookResponse> getBooksByAuthor(
        @PathVariable String authorName,
        @RequestParam(defaultValue = "0") Integer pageNumber,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "asc") String sortOrder) {
        BookResponse books = bookService.getAllBooksByAuthor(authorName, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/getAllBooksByCategory/{category}")
    public ResponseEntity<BookResponse> getBooksByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        BookResponse books = bookService.getAllBooksByCategory(category, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(books);
    }

    @PutMapping("updateBook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable UUID id, @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }


    @DeleteMapping("deleteBook/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? ResponseEntity.ok("Book deleted successfully") :
                ResponseEntity.badRequest().body("Failed to delete book");
    }
}
