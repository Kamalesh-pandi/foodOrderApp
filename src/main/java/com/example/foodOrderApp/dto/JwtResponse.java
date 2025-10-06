package com.example.foodOrderApp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String name;
    private String email;
    private String role;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profileImage;
    private String address;
    private String phoneNumber;
    private String password;
}
