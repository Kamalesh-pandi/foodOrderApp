package com.example.foodOrderApp.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String paymentMethod; // "cod" or "online"

    private String paymentStatus; // "PENDING", "PAID", "FAILED"

    private String status; // "PLACED", "PREPARING", "DELIVERED", "CANCELLED"

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems=new ArrayList<>();

    @Column(nullable = false)
    private double totalAmount;
}
