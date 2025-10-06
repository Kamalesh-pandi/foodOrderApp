package com.example.foodOrderApp.dto;

import com.example.foodOrderApp.entities.OrderItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long foodId;
    private String foodName;
    private String imageUrl;
    private double price;
    private int quantity;
    private double totalPrice;

    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        if (orderItem == null || orderItem.getFood() == null) {
            return null;
        }

        return new OrderItemResponse(
                orderItem.getFood().getId(),
                orderItem.getFood().getName(),
                orderItem.getFood().getImageUrl(),
                orderItem.getPrice() / orderItem.getQuantity(), // price per item
                orderItem.getQuantity(),
                orderItem.getTotal() // total price for this line item
        );
    }
}
