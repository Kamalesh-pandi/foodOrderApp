package com.example.foodOrderApp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profileImage;
    private String role;// "ADMIN" or "USER"
    private String address;

}
