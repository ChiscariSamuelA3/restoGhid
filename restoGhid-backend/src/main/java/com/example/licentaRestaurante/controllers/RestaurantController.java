package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.dto.RestaurantUpdateDTO;
import com.example.licentaRestaurante.models.bookings.DeletedBooking;
import com.example.licentaRestaurante.models.restaurants.AllRestaurants;
import com.example.licentaRestaurante.models.restaurants.SingleRestaurant;
import com.example.licentaRestaurante.services.RecommendationService;
import com.example.licentaRestaurante.services.RestaurantService;
import com.example.licentaRestaurante.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final JwtUtils jwtUtils;
    private final RecommendationService recommendationService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, JwtUtils jwtUtils, RecommendationService recommendationService) {
        this.restaurantService = restaurantService;
        this.jwtUtils = jwtUtils;
        this.recommendationService = recommendationService;
    }

    @GetMapping("{id}")
    public SingleRestaurant getRestaurantById(@PathVariable String id) {
        return restaurantService.findRestaurantById(id);
    }

    @GetMapping
    public AllRestaurants getAllRestaurants() {
        return restaurantService.findAllRestaurants();
    }

    @GetMapping("/favorites")
    public AllRestaurants getRecommendations(@RequestHeader("Authorization") String token) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return recommendationService.generateRecommendations(username);
    }

    @GetMapping("/manager")
    public SingleRestaurant getRestaurantsByManager(@RequestHeader("Authorization") String token) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return restaurantService.findRestaurantByManager(username);
    }

    @PatchMapping("/update")
    public SingleRestaurant updateRestaurant(@RequestHeader("Authorization") String token, @RequestBody RestaurantUpdateDTO restaurant) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return restaurantService.updateRestaurant(username, restaurant);
    }

    @DeleteMapping("/bookings/{id}")
    public DeletedBooking deleteBooking(@PathVariable("id") Long id) {
        restaurantService.deleteBooking(id);

        return new DeletedBooking("Deleted", "", 200);
    }

    @PostMapping("/update-clusters")
    public void updateClusters() {
        restaurantService.updateClusterFromCSV();
    }
}
