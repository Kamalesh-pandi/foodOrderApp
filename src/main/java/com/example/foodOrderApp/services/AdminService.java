package com.example.foodOrderApp.services;

import com.example.foodOrderApp.dto.CategoryOrdersResponse;
import com.example.foodOrderApp.entities.Category;
import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.repositories.CategoryRepository;
import com.example.foodOrderApp.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;

    public List<CategoryOrdersResponse> getCategoriesWithOrders() {
        List<Category> categories = categoryRepo.findAll();

        return categories.stream().map(category -> {
            List<Order> orders = orderRepo.findOrdersByCategoryId(category.getId());
            List<CategoryOrdersResponse.OrderSummary> orderSummaries = orders.stream().map(o ->
                    new CategoryOrdersResponse.OrderSummary(
                            o.getId(),
                            o.getUser().getFullName(), // assuming you have user linked to order
                            o.getTotalAmount()
                    )
            ).toList();

            return new CategoryOrdersResponse(
                    category.getId(),
                    category.getName(),
                    orderSummaries
            );
        }).toList();
    }

    public double getTotalRevenue() {
        return orderRepo.findAll().stream()
                .mapToDouble(order -> order.getTotalAmount() != 0.0 ? order.getTotalAmount() : 0.0)
                .sum();
    }

    public List<Map<String, Object>> getMonthlyRevenue(int year) {
        return orderRepo.getMonthlyRevenue(year);
    }
}

