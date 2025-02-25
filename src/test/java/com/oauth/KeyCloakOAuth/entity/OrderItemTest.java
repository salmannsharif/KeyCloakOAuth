package com.oauth.KeyCloakOAuth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {

    private OrderItem orderItem;
    private MenuItem menuItem;
    private Order order;

    @BeforeEach
    public void setup() {
        orderItem = new OrderItem();
        menuItem = new MenuItem();
        order = new Order();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(orderItem);
        assertNull(orderItem.getId());
        assertNull(orderItem.getQuantity());
        assertNull(orderItem.getMenuItem());
        assertNull(orderItem.getOrder());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        Integer quantity = 2;
        MenuItem testMenuItem = new MenuItem();
        Order testOrder = new Order();

        OrderItem constructedOrderItem = new OrderItem(id, quantity, testMenuItem, testOrder);

        assertEquals(id, constructedOrderItem.getId());
        assertEquals(quantity, constructedOrderItem.getQuantity());
        assertEquals(testMenuItem, constructedOrderItem.getMenuItem());
        assertEquals(testOrder, constructedOrderItem.getOrder());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        Integer quantity = 3;
        MenuItem testMenuItem = new MenuItem();
        Order testOrder = new Order();

        orderItem.setId(id);
        orderItem.setQuantity(quantity);
        orderItem.setMenuItem(testMenuItem);
        orderItem.setOrder(testOrder);

        assertEquals(id, orderItem.getId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(testMenuItem, orderItem.getMenuItem());
        assertEquals(testOrder, orderItem.getOrder());
    }

    @Test
    public void testMenuItemRelationship() {
        Long menuItemId = 1L;
        menuItem.setId(menuItemId);
        orderItem.setMenuItem(menuItem);

        assertEquals(menuItem, orderItem.getMenuItem());
        assertEquals(menuItemId, orderItem.getMenuItem().getId());
    }

    @Test
    public void testOrderRelationship() {
        Long orderId = 1L;
        order.setId(orderId);
        orderItem.setOrder(order);

        assertEquals(order, orderItem.getOrder());
        assertEquals(orderId, orderItem.getOrder().getId());
    }

    @Test
    public void testEdgeCases() {
        // Test null quantity
        orderItem.setQuantity(null);
        assertNull(orderItem.getQuantity());

        // Test zero quantity
        orderItem.setQuantity(0);
        assertEquals(0, orderItem.getQuantity());

        // Test negative quantity
        orderItem.setQuantity(-1);
        assertEquals(-1, orderItem.getQuantity());

        // Test null menuItem
        orderItem.setMenuItem(null);
        assertNull(orderItem.getMenuItem());

        // Test null order
        orderItem.setOrder(null);
        assertNull(orderItem.getOrder());
    }
}