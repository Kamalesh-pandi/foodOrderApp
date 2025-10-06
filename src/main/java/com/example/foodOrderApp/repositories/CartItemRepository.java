package com.example.foodOrderApp.repositories;

import com.example.foodOrderApp.entities.CartItem;
import com.example.foodOrderApp.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndFoodId(User user, Long foodId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId AND c.food.id = :foodId")
    void deleteByUserIdAndFoodId(@Param("userId") Long userId, @Param("foodId") Long foodId);

}
