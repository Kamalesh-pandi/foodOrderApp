package com.example.foodOrderApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    private String address;
    private String paymentMethod;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long foodId;
        private int quantity;
    }
}
