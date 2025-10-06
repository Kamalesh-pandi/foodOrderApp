package com.example.foodOrderApp.repositories;

import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByCategory(Category category);
    List<Food> findByPopularTrue();
    List<Food> findByNewestTrue();
}
