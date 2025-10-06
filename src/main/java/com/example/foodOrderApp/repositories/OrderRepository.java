package com.example.foodOrderApp.repositories;

import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);
    List<Order> findByUser(User user);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi JOIN oi.food f WHERE f.category.id = :categoryId")
    List<Order> findOrdersByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT FUNCTION('MONTH', o.orderDate) as month, SUM(o.totalAmount) as revenue " +
            "FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year " +
            "GROUP BY FUNCTION('MONTH', o.orderDate) ORDER BY month")
    List<Map<String, Object>> getMonthlyRevenue(int year);
}
