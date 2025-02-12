package org.example.bookstore.controller;

import org.example.bookstore.model.Category;
import org.example.bookstore.payload.CategoryDTO;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private static final String ADD_CATEGORY = "Add category successfully";
    private static final String GET_CATEGORY = "Get category successfully";
    private static final String UPDATE_CATEGORY = "Update category successfully";


    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse> addCategory(@RequestParam String name) {
        boolean isSuccess = categoryService.addCategory(name);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message(ADD_CATEGORY)
                .data(isSuccess)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }
    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable UUID id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message(GET_CATEGORY)
                .data(categoryDTO)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<DataResponse> getAllCategories() {
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message(GET_CATEGORY)
                .data(categoryDTOS)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @PutMapping("/updateCategory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse>  updateCategory(@PathVariable UUID id,@RequestBody CategoryDTO categoryDTO) {
        boolean isSuccess = categoryService.updateCategory(id, categoryDTO);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .message(UPDATE_CATEGORY)
                .data(isSuccess)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

}
