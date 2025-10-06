package com.example.foodOrderApp.services;

import com.example.foodOrderApp.dto.FoodRequest;
import com.example.foodOrderApp.entities.Category;
import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.repositories.CategoryRepository;
import com.example.foodOrderApp.repositories.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepo;
    private final CategoryRepository categoryRepo;

    public FoodService(FoodRepository foodRepo, CategoryRepository categoryRepo) {
        this.foodRepo = foodRepo;
        this.categoryRepo = categoryRepo;
    }

    public Food createFood(FoodRequest req) {
        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Food food = Food.builder()
                .name(req.getName())
                .price(req.getPrice())
                .description(req.getDescription())
                .imageUrl(req.getImageUrl())
                .popular(req.isPopular())
                .newest(req.isNewest())
                .category(category)
                .build();
        return foodRepo.save(food);
    }

    public Food updateFood(Long id, FoodRequest req) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id " + id));

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setDescription(req.getDescription());
        food.setImageUrl(req.getImageUrl());
        food.setPopular(req.isPopular());
        food.setNewest(req.isNewest());
        food.setCategory(category);

        return foodRepo.save(food);
    }

    public void deleteFood(Long id) {
        foodRepo.deleteById(id);
    }

    public Optional<Food> getFood(Long id) {
        return foodRepo.findById(id);
    }

    public List<Food> listAll() {
        return foodRepo.findAll();
    }

    public List<Food> listByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return foodRepo.findByCategory(category);
    }

    public List<Food> listPopular() {
        return foodRepo.findByPopularTrue();
    }

    public List<Food> listNewest() {
        return foodRepo.findByNewestTrue();
    }
}
