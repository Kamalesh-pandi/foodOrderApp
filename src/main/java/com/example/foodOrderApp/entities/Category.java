package com.example.foodOrderApp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Fast Food", "Italian", "Drinks"

    private String description;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Food> foods;
}

