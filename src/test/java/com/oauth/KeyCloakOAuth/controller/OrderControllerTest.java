package com.oauth.KeyCloakOAuth.controller;

import com.oauth.KeyCloakOAuth.entity.Order;
import com.oauth.KeyCloakOAuth.entity.OrderItem;
import com.oauth.KeyCloakOAuth.exception.ResourceNotFoundException;
import com.oauth.KeyCloakOAuth.repository.OrderItemRepository;
import com.oauth.KeyCloakOAuth.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        List<OrderItem> orderItems = List.of(orderItem);
        order.setOrderItems(orderItems);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        String orderJson = "{\"id\":1,\"orderItems\":[{\"id\":1}]}";

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderItems[0].id").value(1));

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    public void testGetOrderDetails() throws Exception {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        List<OrderItem> orderItems = List.of(new OrderItem());
        order.setOrderItems(orderItems);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(orderItems);

        // Act & Assert
        mockMvc.perform(get("/orders/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderItemRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    public void testGetOrderDetailsNotFound() throws Exception {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/orders/order/{orderId}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Order not found with id: " + orderId,
                        result.getResolvedException().getMessage()));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderItemRepository, never()).findByOrderId(any());
    }

    @Test
    public void testGetAllOrders() throws Exception {
        // Arrange
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$.length()").value(2));

        verify(orderRepository, times(1)).findAll();
    }
}