package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.OrderResponse;
import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Get all orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponse> response = orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ✅ Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id,
                                                           @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(OrderResponse.fromEntity(updatedOrder));
    }

    // ✅ Update payment status
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<OrderResponse> updatePaymentStatus(@PathVariable Long id,
                                                             @RequestParam String paymentStatus) {
        Order updatedOrder = orderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok(OrderResponse.fromEntity(updatedOrder));
    }
}
