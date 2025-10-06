package com.example.foodOrderApp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long foodId;
    private int quantity;
}

