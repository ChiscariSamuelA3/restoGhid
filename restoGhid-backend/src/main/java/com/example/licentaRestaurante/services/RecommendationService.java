package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Restaurant;
import com.example.licentaRestaurante.entities.Review;
import com.example.licentaRestaurante.entities.User;
import com.example.licentaRestaurante.models.restaurants.AllRestaurants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {
    private final UserSimilarityCalculator userSimilarityCalculator;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @Autowired
    public RecommendationService(UserSimilarityCalculator userSimilarityCalculator, UserService userService, RestaurantService restaurantService) {
        this.userSimilarityCalculator = userSimilarityCalculator;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    public AllRestaurants generateRecommendations(String username) {
        var user = userService.getUserByUsername(username).getData();

        // 1. Find similar users
        List<User> allUsers = userService.getAllUsers().getData();
        List<User> similarUsers = new ArrayList<>();
        for (User u : allUsers) {
            if (!Objects.equals(u.getId(), user.getId())) {
                if (userSimilarityCalculator.calculateSimilarity(user.getUsername(), u.getUsername()) > 0.5) {
                    similarUsers.add(u);
                }
            }
        }

        // if there are no similar users, return findRestaurantsByCluster
        if (similarUsers.isEmpty()) {
            return restaurantService.findRestaurantsByCluster(user.getUsername());
        }

        // 2. Collect ratings for restaurants rated by similar users
        Map<Restaurant, List<Double>> restaurantRatings = new HashMap<>();
        for (User u : similarUsers) {
            for (Review r : u.getReviews()) {
                if (!restaurantRatings.containsKey(r.getRestaurant())) {
                    restaurantRatings.put(r.getRestaurant(), new ArrayList<>());
                }
                restaurantRatings.get(r.getRestaurant()).add(r.getScore());
            }
        }

        // check if the number of restaurants is less than 3, if so, return findRestaurantsByCluster
        if (restaurantRatings.size() < 3) {
            return restaurantService.findRestaurantsByCluster(user.getUsername());
        }

        // 3. calculate the average rating for each restaurant
        Map<Restaurant, Double> restaurantAverageRatings = new HashMap<>();
        for (Restaurant r : restaurantRatings.keySet()) {
            double sum = 0;
            for (Double d : restaurantRatings.get(r)) {
                sum += d;
            }
            restaurantAverageRatings.put(r, sum / restaurantRatings.get(r).size());
        }

        // 4. sort the restaurants by average rating and return the list
        List<Map.Entry<Restaurant, Double>> sortedRestaurants = new ArrayList<>(restaurantAverageRatings.entrySet());
        sortedRestaurants.sort(Map.Entry.comparingByValue());
        Collections.reverse(sortedRestaurants);

        // calculate the mean rating for all restaurants that have been rated by similar users
        double meanRating = 0;
        for (Map.Entry<Restaurant, Double> entry : sortedRestaurants) {
            meanRating += entry.getValue();
        }

        meanRating /= sortedRestaurants.size();

        // if the mean rating is less than 3.5, return findRestaurantsByCluster
        if (meanRating < 3.5) {
            return restaurantService.findRestaurantsByCluster(user.getUsername());
        }

        List<Restaurant> recommendations = new ArrayList<>();
        for (Map.Entry<Restaurant, Double> entry : sortedRestaurants) {
            recommendations.add(entry.getKey());
        }

        var allRestaurants = new AllRestaurants();
        allRestaurants.setData(recommendations);
        allRestaurants.setStatusCode(200);

        return allRestaurants;
    }
}