package com.example.foodOrderApp.services;

import com.example.foodOrderApp.dto.UpdateProfileRequest;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder=passwordEncoder;
    }

    /**
     * Given Spring Security UserDetails, find your application's User ID
     */
    public Long getUserIdFromUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername(); // assuming username = email
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    /**
     * Optional: Get User entity from UserDetails
     */
    public User getUserFromUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfileByEmail(String email, UpdateProfileRequest request) throws IOException {
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        User user = optionalUser.get();

        // Update only if new values are provided
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }

        // Handle profile image
        MultipartFile profileImage = request.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            // You can save the file to DB as Base64 OR to a storage location
            byte[] imageBytes = profileImage.getBytes();
            // Example: save as Base64 string
            String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
            user.setProfileImage("data:image/png;base64," + base64Image);
        }

        return userRepo.save(user);
    }
}
