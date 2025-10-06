package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.CategoryOrdersResponse;
import com.example.foodOrderApp.services.AdminService;
import com.example.foodOrderApp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;

    @GetMapping("/categories-with-orders")
    public ResponseEntity<List<CategoryOrdersResponse>> getCategoriesWithOrders() {
        return ResponseEntity.ok(adminService.getCategoriesWithOrders());
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<Map<String, Object>> getTotalRevenue() {
        double totalRevenue = adminService.getTotalRevenue();
        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-revenue/{year}")
    public List<Map<String, Object>> getMonthlyRevenue(@PathVariable int year) {
        return adminService.getMonthlyRevenue(year);
    }

}
