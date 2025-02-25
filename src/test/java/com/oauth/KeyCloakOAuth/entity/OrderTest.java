package com.oauth.KeyCloakOAuth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order order;
    private OrderItem orderItem1;
    private OrderItem orderItem2;

    @BeforeEach
    public void setup() {
        order = new Order();
        orderItem1 = new OrderItem();
        orderItem2 = new OrderItem();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getCustomerName());
        assertNull(order.getOrderItems());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String customerName = "John Doe";
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        Order constructedOrder = new Order(id, customerName, orderItems);

        assertEquals(id, constructedOrder.getId());
        assertEquals(customerName, constructedOrder.getCustomerName());
        assertEquals(orderItems, constructedOrder.getOrderItems());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String customerName = "Jane Smith";
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        order.setId(id);
        order.setCustomerName(customerName);
        order.setOrderItems(orderItems);

        assertEquals(id, order.getId());
        assertEquals(customerName, order.getCustomerName());
        assertEquals(orderItems, order.getOrderItems());
    }

    @Test
    public void testOrderItemsRelationship() {
        orderItem1.setId(1L);
        orderItem2.setId(2L);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        order.setOrderItems(orderItems);
        orderItem1.setOrder(order);
        orderItem2.setOrder(order);

        assertEquals(2, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem1));
        assertTrue(order.getOrderItems().contains(orderItem2));
        assertEquals(order, orderItem1.getOrder());
        assertEquals(order, orderItem2.getOrder());
    }

    @Test
    public void testEdgeCases() {
        // Test null customer name
        order.setCustomerName(null);
        assertNull(order.getCustomerName());

        // Test empty order items list
        order.setOrderItems(List.of());
        assertTrue(order.getOrderItems().isEmpty());

        // Test null order items
        order.setOrderItems(null);
        assertNull(order.getOrderItems());
    }
}