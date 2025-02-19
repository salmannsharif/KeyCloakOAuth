package com.oauth.KeyCloakOAuth.controller;

import com.oauth.KeyCloakOAuth.entity.Menu;
import com.oauth.KeyCloakOAuth.entity.MenuItem;
import com.oauth.KeyCloakOAuth.entity.Restaurant;
import com.oauth.KeyCloakOAuth.repository.MenuItemRepository;
import com.oauth.KeyCloakOAuth.repository.MenuRepository;
import com.oauth.KeyCloakOAuth.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // Public API: Get menu for a specific restaurant
    @GetMapping("/public/{restaurantId}/menu")
    public List<Menu> getMenu(@PathVariable Long restaurantId) {
        return menuRepository.findAll().stream()
                .filter(menu -> menu.getRestaurant().getId().equals(restaurantId))
                .toList();
    }

    // Public API: Get all restaurants
    @GetMapping("/public/getAllRestaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // Manager-only API: Create a menu for a specific restaurant
    @PostMapping("/{restaurantId}/menu")
    @PreAuthorize("hasRole('manager')")
    public Menu createMenu(@RequestBody Menu menu, @PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);
    }

    // Owner-only API: Update the price of a menu item
    @PutMapping("/menu-item/{menuItemId}/price")
    @PreAuthorize("hasRole('owner')")
    public MenuItem updateMenuItemPrice(@PathVariable Long menuItemId, @RequestParam Double price) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
        menuItem.setPrice(price);
        return menuItemRepository.save(menuItem);
    }

    // Admin-only API: Create a new restaurant
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}