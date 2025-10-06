package com.example.foodOrderApp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequest {
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private boolean popular;
    private boolean newest;
    private double rating;
    private Long categoryId; // Admin will send categoryId
}
