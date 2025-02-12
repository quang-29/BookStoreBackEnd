package org.example.bookstore.service.Interface;

import org.example.bookstore.model.Book;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.request.CreateBookRequest;
import org.example.bookstore.payload.response.BookResponse;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.payload.response.CreateBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.Long;
import java.util.List;
import java.util.UUID;


public interface BookService {
    BookDTO getBookById(UUID id);
    BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookResponse getAllBooksByAuthor(String author,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookResponse getAllBooksByCategory(String category,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookDTO addBook(CreateBookRequest request);
    CloudinaryResponse uploadImageBook(UUID id, MultipartFile file);
    String uploadImageB(UUID id, MultipartFile file);

    BookDTO updateBook(UUID id, BookDTO bookDTO);
    boolean deleteBook(UUID id);


}
