package com.example.foodOrderApp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private String imageUrl;
    private String description;
}