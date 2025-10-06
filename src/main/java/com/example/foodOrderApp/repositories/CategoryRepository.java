package com.example.foodOrderApp.repositories;

import com.example.foodOrderApp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
