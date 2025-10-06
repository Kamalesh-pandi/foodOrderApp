package com.example.foodOrderApp.services;

import com.example.foodOrderApp.dto.PlaceOrderRequest;
import com.example.foodOrderApp.entities.Food;
import com.example.foodOrderApp.entities.Order;
import com.example.foodOrderApp.entities.OrderItem;
import com.example.foodOrderApp.entities.User;
import com.example.foodOrderApp.repositories.FoodRepository;
import com.example.foodOrderApp.repositories.OrderItemRepository;
import com.example.foodOrderApp.repositories.OrderRepository;
import com.example.foodOrderApp.repositories.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public OrderService(OrderRepository orderRepository,
                        FoodRepository foodRepository,
                        UserRepository userRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
        this.orderItemRepository=orderItemRepository;
    }

    // -------------------- USER METHODS --------------------

    // Get all orders for a user
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    // Get a single order by id for a user
    public Order getOrderByIdForUser(User user, Long orderId) {
        return orderRepository.findById(orderId)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Order not found or access denied"));
    }

    // Place an order for a user
    public Order placeOrder(User user, PlaceOrderRequest request) throws Exception {
        String address = request.getAddress();
        String paymentMethod = request.getPaymentMethod();
        List<Map<String, Object>> items = request.getItems().stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("foodId", item.getFoodId());
            map.put("quantity", item.getQuantity());
            return map;
        }).toList();

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (Map<String, Object> item : items) {
            Long foodId = Long.valueOf(item.get("foodId").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());

            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setFood(food);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(food.getPrice() * quantity);
            totalAmount += orderItem.getPrice()+30;
            orderItem.setTotal(totalAmount);
            orderItems.add(orderItem);


        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentMethod.equalsIgnoreCase("cod") ? "PENDING" : "PAID");
        order.setStatus("PENDING");
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        // Fix: use regular for loop to set the order in each OrderItem
        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
        }

        // Razorpay integration for online payment
        if (paymentMethod.equalsIgnoreCase("online")) {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            JSONObject options = new JSONObject();
            options.put("amount", (int) (totalAmount * 100)); // amount in paise
            options.put("currency", "INR");

            // Razorpay receipt max length = 40, so use substring if needed
            String receipt = "order_" + UUID.randomUUID().toString().replace("-", "");
            if (receipt.length() > 40) {
                receipt = receipt.substring(0, 40);
            }
            options.put("receipt", receipt);

            com.razorpay.Order razorpayOrder = client.orders.create(options);
            order.setRazorpayOrderId(razorpayOrder.get("id"));
        }

        return orderRepository.save(order);
    }




    // Verify Razorpay payment
    public boolean verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            Utils.verifySignature(payload, razorpaySignature, keySecret);

            Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setPaymentStatus("PAID");
            order.setRazorpayPaymentId(razorpayPaymentId);
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------- ADMIN METHODS --------------------

    // Get all orders (admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Update order status (admin)
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Update payment status (admin)
    public Order updatePaymentStatus(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentStatus(paymentStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        // Delete all order items first
        orderItemRepository.deleteByOrderId(orderId);
        // Then delete the order
        orderRepository.deleteById(orderId);
    }




}
