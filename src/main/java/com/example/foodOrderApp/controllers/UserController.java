package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.UpdateProfileRequest;
import com.example.foodOrderApp.dto.UserResponse;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) MultipartFile profileImage,
            Authentication authentication) throws IOException {

        String loggedInEmail = authentication.getName(); // ✅ This is email from JWT

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setPassword(password);
        request.setAddress(address);
        request.setProfileImage(profileImage);

        User updatedUser = userService.updateProfileByEmail(loggedInEmail, request); // 🔄 service updates by email
        return ResponseEntity.ok(UserResponse.fromEntity(updatedUser));
    }

}
