package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.CategoryRequest;
import com.example.foodOrderApp.dto.CategoryResponse;
import com.example.foodOrderApp.entities.Category;
import com.example.foodOrderApp.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest req) {
        Category created = categoryService.createCategory(req);
        return ResponseEntity.created(URI.create("/api/admin/categories/" + created.getId()))
                .body(CategoryResponse.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest req) {
        Category updated = categoryService.updateCategory(id, req);
        return ResponseEntity.ok(CategoryResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(c -> ResponseEntity.ok(CategoryResponse.fromEntity(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        List<CategoryResponse> res = categoryService.listAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }
}
