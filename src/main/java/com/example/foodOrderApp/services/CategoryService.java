package com.example.foodOrderApp.services;

import com.example.foodOrderApp.dto.CategoryRequest;
import com.example.foodOrderApp.entities.Category;
import com.example.foodOrderApp.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Category createCategory(CategoryRequest req) {
        Category category = Category.builder()
                .name(req.getName())
                .description(req.getDescription())  // ✅ add description
                .imageUrl(req.getImageUrl())        // ✅ add imageUrl
                .build();
        return categoryRepo.save(category);
    }


    public Category updateCategory(Long id, CategoryRequest req) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        category.setImageUrl(req.getImageUrl());
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public Optional<Category> getCategory(Long id) {
        return categoryRepo.findById(id);
    }

    public List<Category> listAll() {
        return categoryRepo.findAll();
    }
}
