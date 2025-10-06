package com.example.foodOrderApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOrdersResponse {
    private Long categoryId;
    private String categoryName;
    private List<OrderSummary> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderSummary {
        private Long orderId;
        private String customerName;
        private Double totalAmount;
    }
}
