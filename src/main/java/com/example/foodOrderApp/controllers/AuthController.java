package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.JwtResponse;
import com.example.foodOrderApp.dto.LoginRequest;
import com.example.foodOrderApp.dto.SignupRequest;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.services.AuthService;
import com.example.foodOrderApp.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody SignupRequest request) {
        User user = authService.registerUser(request);

        // Optional: auto-login after signup
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT with roles
        String token = jwtUtil.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getProfileImage(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getPassword()

        ));
    }

    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT with roles
        String token = jwtUtil.generateToken(authentication);

        // Fetch user details
        User user = authService.findByEmail(request.getEmail());

        return ResponseEntity.ok(new JwtResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getProfileImage(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getPassword()
        ));
    }
}