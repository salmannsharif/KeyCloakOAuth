package com.oauth.KeyCloakOAuth.controller;


import com.oauth.KeyCloakOAuth.entity.Menu;
import com.oauth.KeyCloakOAuth.entity.MenuItem;
import com.oauth.KeyCloakOAuth.entity.Restaurant;
import com.oauth.KeyCloakOAuth.repository.MenuItemRepository;
import com.oauth.KeyCloakOAuth.repository.MenuRepository;
import com.oauth.KeyCloakOAuth.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/{restaurantId}/menu")
    // public api
    public List<Menu> getMenu(@PathVariable Long restaurantId) {
        return menuRepository.findAll().stream()
                .filter(menu -> menu.getRestaurant().getId().equals(restaurantId))
                .toList();
    }

    @GetMapping
    // public api
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @PostMapping("/{restaurantId}/menu")
    // manager can access
    public Menu createMenu(@RequestBody Menu menu, @PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        menu.setRestaurant(restaurant);
        return menuRepository.save(menu);
    }

    @PutMapping("/menu-item/{menuItemId}/price")
    // owner can access
    public MenuItem updateMenuItemPrice(@PathVariable Long menuItemId, @RequestParam Double price) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow();
        menuItem.setPrice(price);
        return menuItemRepository.save(menuItem);
    }


    @PostMapping
    // admin can access
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }


}
