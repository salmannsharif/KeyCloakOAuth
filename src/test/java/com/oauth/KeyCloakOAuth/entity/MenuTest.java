package com.oauth.KeyCloakOAuth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    private Menu menu;
    private Restaurant restaurant;
    private MenuItem menuItem1;
    private MenuItem menuItem2;

    @BeforeEach
    public void setup() {
        menu = new Menu();
        restaurant = new Restaurant();
        menuItem1 = new MenuItem();
        menuItem2 = new MenuItem();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(menu);
        assertNull(menu.getId());
        assertNull(menu.getName());
        assertNull(menu.getRestaurant());
        assertNull(menu.getItems());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String name = "Dinner Menu";
        List<MenuItem> items = Arrays.asList(menuItem1, menuItem2);

        Menu constructedMenu = new Menu(id, name, restaurant, items);

        assertEquals(id, constructedMenu.getId());
        assertEquals(name, constructedMenu.getName());
        assertEquals(restaurant, constructedMenu.getRestaurant());
        assertEquals(items, constructedMenu.getItems());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String name = "Lunch Menu";
        List<MenuItem> items = Arrays.asList(menuItem1, menuItem2);

        menu.setId(id);
        menu.setName(name);
        menu.setRestaurant(restaurant);
        menu.setItems(items);

        assertEquals(id, menu.getId());
        assertEquals(name, menu.getName());
        assertEquals(restaurant, menu.getRestaurant());
        assertEquals(items, menu.getItems());
    }

    @Test
    public void testRestaurantRelationship() {
        Long restaurantId = 1L;
        restaurant.setId(restaurantId);
        menu.setRestaurant(restaurant);

        assertEquals(restaurant, menu.getRestaurant());
        assertEquals(restaurantId, menu.getRestaurant().getId());
    }

    @Test
    public void testMenuItemsRelationship() {
        menuItem1.setId(1L);
        menuItem2.setId(2L);
        List<MenuItem> items = Arrays.asList(menuItem1, menuItem2);
        menu.setItems(items);

        assertEquals(2, menu.getItems().size());
        assertTrue(menu.getItems().contains(menuItem1));
        assertTrue(menu.getItems().contains(menuItem2));
    }

}