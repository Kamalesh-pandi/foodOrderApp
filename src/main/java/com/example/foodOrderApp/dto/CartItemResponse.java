package com.example.foodOrderApp.dto;

import com.example.foodOrderApp.entities.CartItem;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long foodId;
    private String foodName;
    private double price;
    private String imageUrl;
    private int quantity;
    private double totalPrice;

    public static CartItemResponse fromEntity(CartItem item) {
        if (item == null || item.getFood() == null) {
            return null;
        }
        return new CartItemResponse(
                item.getId(),
                item.getFood().getId(),
                item.getFood().getName(),
                item.getFood().getPrice(),
                item.getFood().getImageUrl(),
                item.getQuantity(),
                item.getQuantity() * item.getFood().getPrice());
    }
}
