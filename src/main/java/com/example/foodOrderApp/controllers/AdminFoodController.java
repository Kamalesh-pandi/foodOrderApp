package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.FoodRequest;
import com.example.foodOrderApp.dto.FoodResponse;
import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/foods")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFoodController {

    private final FoodService foodService;

    public AdminFoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody FoodRequest req) {
        Food created = foodService.createFood(req);
        return ResponseEntity.created(URI.create("/api/admin/foods/" + created.getId()))
                .body(FoodResponse.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponse> updateFood(@PathVariable Long id, @RequestBody FoodRequest req) {
        Food updated = foodService.updateFood(id, req);
        return ResponseEntity.ok(FoodResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFood(@PathVariable Long id) {
        return foodService.getFood(id)
                .map(f -> ResponseEntity.ok(FoodResponse.fromEntity(f)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
}
