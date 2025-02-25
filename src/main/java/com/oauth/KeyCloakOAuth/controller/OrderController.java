package com.oauth.KeyCloakOAuth.controller;

import com.oauth.KeyCloakOAuth.entity.Order;
import com.oauth.KeyCloakOAuth.entity.OrderItem;
import com.oauth.KeyCloakOAuth.exception.ResourceNotFoundException;
import com.oauth.KeyCloakOAuth.repository.OrderItemRepository;
import com.oauth.KeyCloakOAuth.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        // authenticated user can access
        orderRepository.save(order);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> {
            orderItem.setId(order.getId());
            orderItemRepository.save(orderItem);
        });
        return order;
    }

    @GetMapping("/order/{orderId}")
    // manager can access
    public Order getOrderDetails(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setOrderItems(orderItemRepository.findByOrderId(order.getId()));
        return order;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}