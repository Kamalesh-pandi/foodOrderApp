package com.example.foodOrderApp.controllers;

import com.example.foodOrderApp.dto.OrderResponse;
import com.example.foodOrderApp.dto.PlaceOrderRequest;
import com.example.foodOrderApp.dto.VerifyPaymentRequest;
import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.services.OrderService;
import com.example.foodOrderApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // ✅ Get all orders of the logged-in user
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        List<Order> orders = orderService.getUserOrders(user);

        List<OrderResponse> response = orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ✅ Get a single order by ID for the logged-in user
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserFromUserDetails(userDetails);
        Order order = orderService.getOrderByIdForUser(user, id);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    // ✅ Place a new order
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PlaceOrderRequest request) throws Exception {
        User user = userService.getUserFromUserDetails(userDetails);
        Order order = orderService.placeOrder(user, request);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    // ✅ Verify Razorpay Payment
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        boolean valid = orderService.verifyPayment(
                request.getRazorpay_payment_id(),
                request.getRazorpay_order_id(),
                request.getRazorpay_signature());
        return ResponseEntity.ok(valid ? "Payment Verified" : "Payment Failed");
    }

    // ---------------- Delete Order ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }



}
