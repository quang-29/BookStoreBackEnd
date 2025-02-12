package org.example.bookstore.service.Interface;

import org.example.bookstore.model.Category;
import org.example.bookstore.payload.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface CategoryService {
    boolean addCategory(String name);
    CategoryDTO getCategoryById(UUID id);
    List<CategoryDTO> getAllCategories();
    boolean updateCategory(UUID id, CategoryDTO categoryDTO);


}
