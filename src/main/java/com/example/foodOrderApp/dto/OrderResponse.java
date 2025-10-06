package com.example.foodOrderApp.dto;

import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.entities.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> items;
    private double totalAmount;
    private String address;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private String userName;
    private String orderDate;

    // constructors, getters, setters

    public static OrderResponse fromEntity(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());

        double total = itemResponses.stream()
                .mapToDouble(OrderItemResponse::getTotalPrice)
                .sum();

        return new OrderResponse(
                order.getId(),
                itemResponses,
                total,
                order.getAddress(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getStatus(),
                order.getUser().getFullName(),
                order.getOrderDate() != null
                        ? order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : null);
    }
}
