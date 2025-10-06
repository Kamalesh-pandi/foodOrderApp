package com.example.foodOrderApp.dto;

import com.example.foodOrderApp.entities.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;

    public static CategoryResponse fromEntity(Category c) {
        if (c == null) {
            return null;
        }
        return new CategoryResponse(c.getId(), c.getName(), c.getImageUrl(),c.getDescription());
    }
}