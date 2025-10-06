package com.example.foodOrderApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who owns the cart
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Food item added
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private int quantity;
}
