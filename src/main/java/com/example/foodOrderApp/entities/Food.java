package com.example.foodOrderApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private String description;

    private String imageUrl;

    private double rating;

    private boolean popular;

    private boolean newest;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
