package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.CartItemRequest;
import com.example.foodOrderApp.dto.CartItemResponse;
import com.example.foodOrderApp.entities.CartItem;
import com.example.foodOrderApp.services.CartService;
import com.example.foodOrderApp.services.UserService; // optional helper to get current user
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartItemRequest req) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        CartItem cartItem = cartService.addToCart(userId, req.getFoodId(), req.getQuantity());
        return ResponseEntity.ok(CartItemResponse.fromEntity(cartItem));
    }

    // Update quantity
    @PutMapping("/update")
    public ResponseEntity<CartItemResponse> updateCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartItemRequest req) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        CartItem cartItem = cartService.updateCartItem(userId, req.getFoodId(), req.getQuantity());
        return ResponseEntity.ok(CartItemResponse.fromEntity(cartItem));
    }

    // Remove item
    @DeleteMapping("/remove/{foodId}")
    public ResponseEntity<?> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long foodId) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        cartService.removeCartItem(userId, foodId);
        return ResponseEntity.noContent().build();
    }

    // List all items
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> listCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        List<CartItem> items = cartService.listCartItems(userId);
        List<CartItemResponse> res = items.stream().map(CartItemResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    // Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
