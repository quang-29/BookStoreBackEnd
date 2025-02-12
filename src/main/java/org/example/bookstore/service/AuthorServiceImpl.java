package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Author;
import org.example.bookstore.payload.AuthorDTO;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.service.Interface.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        boolean existedAuthor = authorRepository.existsByName(authorDTO.getAuthorName());
        if(existedAuthor){
            throw new AppException(ErrorCode.AUTHOR_EXISTED);
        }
        Author author = modelMapper.map(authorDTO, Author.class);
        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorDTO.class);

    }

    @Override
    public AuthorDTO updateAuthor(UUID id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.AUTHOR_NOT_FOUND)
        );
        author.setName(authorDTO.getAuthorName());
        author.setBio(authorDTO.getBio());
        author.setEmail(authorDTO.getEmail());
        author.setPhone(authorDTO.getPhone());
        author.setAddress(authorDTO.getAddress());
        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, AuthorDTO.class);
    }

    @Override
    public boolean deleteAuthor(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        authorRepository.delete(author);
        return true;
    }

    @Override
    public AuthorDTO getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return modelMapper.map(author, AuthorDTO.class);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class)).collect(Collectors.toList());
    }

    @Override
    public AuthorDTO getAuthorByName(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(()-> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return modelMapper.map(author, AuthorDTO.class);
    }
}
