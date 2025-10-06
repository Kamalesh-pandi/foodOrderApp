package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.FoodResponse;
import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/foods") // public API for users
public class PublicFoodController {

    private final FoodService foodService;

    public PublicFoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    // Get all foods (optional category filter)
    @GetMapping
    public ResponseEntity<List<FoodResponse>> listFoods(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean popular,
            @RequestParam(required = false) Boolean newest) {
        List<Food> foods;

        if (categoryId != null) {
            foods = foodService.listByCategory(categoryId);
        } else if (popular != null && popular) {
            foods = foodService.listPopular();
        } else if (newest != null && newest) {
            foods = foodService.listNewest();
        } else {
            foods = foodService.listAll();
        }

        List<FoodResponse> res = foods.stream().map(FoodResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    // Get single food by id
    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFood(@PathVariable Long id) {
        return foodService.getFood(id)
                .map(f -> ResponseEntity.ok(FoodResponse.fromEntity(f)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
