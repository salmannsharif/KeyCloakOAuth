package com.oauth.KeyCloakOAuth.controller;

import com.oauth.KeyCloakOAuth.entity.Menu;
import com.oauth.KeyCloakOAuth.entity.MenuItem;
import com.oauth.KeyCloakOAuth.entity.Restaurant;
import com.oauth.KeyCloakOAuth.repository.MenuItemRepository;
import com.oauth.KeyCloakOAuth.repository.MenuRepository;
import com.oauth.KeyCloakOAuth.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testGetMenu() throws Exception {

        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setRestaurant(restaurant);

        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setRestaurant(restaurant);

        List<Menu> menus = Arrays.asList(menu1, menu2);
        when(menuRepository.findAll()).thenReturn(menus);

        mockMvc.perform(get("/restaurants/public/{restaurantId}/menu", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$.length()").value(2));

        verify(menuRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllRestaurants() throws Exception {

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(2L);

        List<Restaurant> restaurantList = Arrays.asList(restaurant1, restaurant2);
        when(restaurantRepository.findAll()).thenReturn(restaurantList);

        mockMvc.perform(get("/restaurants/public/getAllRestaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$.length()").value(2));

        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "manager")
    public void testCreateMenu() throws Exception {

        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Menu menu = new Menu();
        menu.setId(1L);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        String menuJson = "{\"id\":1}";

        mockMvc.perform(post("/restaurants/{restaurantId}/menu", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    @WithMockUser(roles = "manager")
    public void testCreateMenuRestaurantNotFound() throws Exception {
        Long restaurantId = 1L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        String menuJson = "{\"id\":1}";

        mockMvc.perform(post("/restaurants/{restaurantId}/menu", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuJson))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertInstanceOf(ResponseStatusException.class, result.getResolvedException());
                    ResponseStatusException ex = (ResponseStatusException) result.getResolvedException();
                    assertEquals("Restaurant not found", ex.getReason());
                });

        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuRepository, never()).save(any());
    }

    @Test
    @WithMockUser(roles = "owner")
    public void testUpdateMenuItemPrice() throws Exception {

        Long menuItemId = 1L;
        Double newPrice = 15.87;
        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setPrice(newPrice);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        mockMvc.perform(put("/restaurants/menu-item/{menuItemId}/price", menuItemId)
                        .param("price", String.valueOf(newPrice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(menuItemId))
                .andExpect(jsonPath("$.price").value(newPrice));

        verify(menuItemRepository, times(1)).findById(menuItemId);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @WithMockUser(roles = "owner")
    public void testUpdateMenuItemPriceNotFound() throws Exception {
        Long menuItemId = 1L;
        Double newPrice = 9.99;
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/restaurants/menu-item/{menuItemId}/price", menuItemId)
                        .param("price", String.valueOf(newPrice)))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertInstanceOf(ResponseStatusException.class, result.getResolvedException());
                    ResponseStatusException ex = (ResponseStatusException) result.getResolvedException();
                    assertEquals("Menu item not found", ex.getReason());
                });

        verify(menuItemRepository, times(1)).findById(menuItemId);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void testCreateRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        String restaurantJson = "{\"id\":1}";

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(restaurantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
}