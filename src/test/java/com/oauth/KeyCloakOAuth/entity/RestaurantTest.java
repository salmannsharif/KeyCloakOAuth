package com.oauth.KeyCloakOAuth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    private Restaurant restaurant;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    public void setup() {
        restaurant = new Restaurant();
        menu1 = new Menu();
        menu2 = new Menu();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(restaurant);
        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getLocation());
        assertNull(restaurant.getMenus());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String name = "Test Restaurant";
        String location = "123 Main St";
        List<Menu> menus = Arrays.asList(menu1, menu2);

        Restaurant constructedRestaurant = new Restaurant(id, name, location, menus);

        assertEquals(id, constructedRestaurant.getId());
        assertEquals(name, constructedRestaurant.getName());
        assertEquals(location, constructedRestaurant.getLocation());
        assertEquals(menus, constructedRestaurant.getMenus());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String name = "Another Restaurant";
        String location = "456 Oak Ave";
        List<Menu> menus = Arrays.asList(menu1, menu2);

        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setLocation(location);
        restaurant.setMenus(menus);

        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(location, restaurant.getLocation());
        assertEquals(menus, restaurant.getMenus());
    }

    @Test
    public void testMenusRelationship() {
        menu1.setId(1L);
        menu2.setId(2L);
        List<Menu> menus = Arrays.asList(menu1, menu2);

        restaurant.setMenus(menus);
        menu1.setRestaurant(restaurant);
        menu2.setRestaurant(restaurant);

        assertEquals(2, restaurant.getMenus().size());
        assertTrue(restaurant.getMenus().contains(menu1));
        assertTrue(restaurant.getMenus().contains(menu2));
        assertEquals(restaurant, menu1.getRestaurant());
        assertEquals(restaurant, menu2.getRestaurant());
    }

    @Test
    public void testEdgeCases() {
        // Test null name
        restaurant.setName(null);
        assertNull(restaurant.getName());

        // Test null location
        restaurant.setLocation(null);
        assertNull(restaurant.getLocation());

        // Test empty menus list
        restaurant.setMenus(List.of());
        assertTrue(restaurant.getMenus().isEmpty());

        // Test null menus
        restaurant.setMenus(null);
        assertNull(restaurant.getMenus());
    }
}