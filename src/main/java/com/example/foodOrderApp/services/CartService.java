package com.example.foodOrderApp.services;

import com.example.foodOrderApp.entities.CartItem;
import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.repositories.CartItemRepository;
import com.example.foodOrderApp.repositories.FoodRepository;
import com.example.foodOrderApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final UserRepository userRepo;
    private final FoodRepository foodRepo;

    public CartService(CartItemRepository cartRepo, UserRepository userRepo, FoodRepository foodRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.foodRepo = foodRepo;
    }

    // Add item to cart
    public CartItem addToCart(Long userId, Long foodId, int quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Food food = foodRepo.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // If item already exists, update quantity
        CartItem cartItem = cartRepo.findByUserAndFoodId(user, foodId)
                .orElse(CartItem.builder().user(user).food(food).quantity(0).build());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartRepo.save(cartItem);
    }

    // Update quantity
    public CartItem updateCartItem(Long userId, Long foodId, int quantity) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItem cartItem = cartRepo.findByUserAndFoodId(user, foodId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        return cartRepo.save(cartItem);
    }

    // Remove item
    public void removeCartItem(Long userId, Long foodId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartRepo.deleteByUserIdAndFoodId(userId, foodId);
    }

    // List all items
    public List<CartItem> listCartItems(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepo.findByUser(user);
    }

    // Clear cart
    public void clearCart(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> items = cartRepo.findByUser(user);
        cartRepo.deleteAll(items);
    }
}
