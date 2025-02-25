package com.oauth.KeyCloakOAuth.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {

    private MenuItem menuItem;
    private Menu menu;

    @BeforeEach
    public void setup() {
        menuItem = new MenuItem();
        menu = new Menu();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(menuItem);
        assertNull(menuItem.getId());
        assertNull(menuItem.getName());
        assertEquals(0.0, menuItem.getPrice(), 0.001); // double default value is 0.0
        assertNull(menuItem.getMenu());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String name = "Burger";
        double price = 9.99;

        MenuItem constructedMenuItem = new MenuItem(id, name, price, menu);

        assertEquals(id, constructedMenuItem.getId());
        assertEquals(name, constructedMenuItem.getName());
        assertEquals(price, constructedMenuItem.getPrice(), 0.001);
        assertEquals(menu, constructedMenuItem.getMenu());
    }

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String name = "Pizza";
        double price = 12.99;

        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setPrice(price);
        menuItem.setMenu(menu);

        assertEquals(id, menuItem.getId());
        assertEquals(name, menuItem.getName());
        assertEquals(price, menuItem.getPrice(), 0.001);
        assertEquals(menu, menuItem.getMenu());
    }

    @Test
    public void testMenuRelationship() {
        Long menuId = 1L;
        menu.setId(menuId);
        menuItem.setMenu(menu);

        assertEquals(menu, menuItem.getMenu());
        assertEquals(menuId, menuItem.getMenu().getId());
    }

    @Test
    public void testJacksonSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Long id = 1L;
        String name = "Salad";
        double price = 7.99;
        menu.setId(1L);

        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setPrice(price);
        menuItem.setMenu(menu);

        // Serialize to JSON
        String json = mapper.writeValueAsString(menuItem);
        System.out.println("Serialized JSON: " + json); // For debugging

        // Deserialize back to object
        MenuItem deserializedMenuItem = mapper.readValue(json, MenuItem.class);

        // Assertions
        assertEquals(id, deserializedMenuItem.getId());
        assertEquals(name, deserializedMenuItem.getName());
        assertEquals(price, deserializedMenuItem.getPrice(), 0.001);
        // Menu will be null due to @JsonBackReference
        assertNull(deserializedMenuItem.getMenu(), "Menu should be null due to JsonBackReference");
    }

    @Test
    public void testEdgeCases() {
        // Test null name
        menuItem.setName(null);
        assertNull(menuItem.getName());

        // Test negative price
        menuItem.setPrice(-5.0);
        assertEquals(-5.0, menuItem.getPrice(), 0.001);

        // Test zero price
        menuItem.setPrice(0.0);
        assertEquals(0.0, menuItem.getPrice(), 0.001);
    }
}