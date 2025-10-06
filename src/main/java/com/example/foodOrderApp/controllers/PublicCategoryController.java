package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.CategoryResponse;
import com.example.foodOrderApp.entities.Category;
import com.example.foodOrderApp.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories") // public endpoint
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        List<CategoryResponse> res = categoryService.listAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    // Get single category by id (optional)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(c -> ResponseEntity.ok(CategoryResponse.fromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
