package com.example.foodOrderApp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private String address;
    private MultipartFile profileImage;
}
