package com.example.foodOrderApp.dto;

import com.example.foodOrderApp.entities.Food;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private double rating;
    private boolean popular;
    private boolean newest;
    private String CategoryName;

    public static FoodResponse fromEntity(Food f) {
        if (f == null) {
            return null;
        }
        return new FoodResponse(
                f.getId(),
                f.getName(),
                f.getPrice(),
                f.getDescription(),
                f.getImageUrl(),
                f.getRating(),
                f.isPopular(),
                f.isNewest(),
                f.getCategory() != null ? f.getCategory().getName() : null);
    }
}
