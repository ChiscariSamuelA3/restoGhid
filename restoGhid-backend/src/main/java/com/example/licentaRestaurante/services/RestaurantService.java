package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.dto.RestaurantUpdateDTO;
import com.example.licentaRestaurante.entities.Restaurant;
import com.example.licentaRestaurante.models.restaurants.AllRestaurants;
import com.example.licentaRestaurante.models.restaurants.SingleRestaurant;
import com.example.licentaRestaurante.repositories.BookingRepository;
import com.example.licentaRestaurante.repositories.RestaurantRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, BookingRepository bookingRepository) {
        this.restaurantRepository = restaurantRepository;
        this.bookingRepository = bookingRepository;
    }

    public SingleRestaurant findRestaurantById(String id) {
        var singleRestaurant = new SingleRestaurant();
        try {
            Restaurant restaurant = restaurantRepository.findById(id).get();
            singleRestaurant.setData(restaurant);
            singleRestaurant.setStatusCode(200);
        } catch (Exception e) {
            singleRestaurant.setError("Restaurant not found");
            singleRestaurant.setStatusCode(404);
        }
        return singleRestaurant;
    }

    public AllRestaurants findAllRestaurants() {
        var allRestaurants = new AllRestaurants();
        try {
            allRestaurants.setData(restaurantRepository.findAll());
            allRestaurants.setStatusCode(200);
        } catch (Exception e) {
            allRestaurants.setError("Restaurants not found");
            allRestaurants.setStatusCode(404);
        }
        return allRestaurants;
    }

    public void updateClusterFromCSV() {
        try {
            Resource resource = new ClassPathResource("data/clusters_all.csv");
            Reader reader = new FileReader(resource.getFile());
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> records = csvReader.readAll();
            for (String[] record : records) {
                Restaurant restaurant = restaurantRepository.findByLocationId(record[0]);
                if (restaurant == null) {
                    continue;
                }
                restaurant.setCluster(Integer.parseInt(record[4]));
                restaurantRepository.save(restaurant);
            }
        } catch (IOException | CsvException e) {
            System.out.println(e.getMessage());
        }
    }

    public Restaurant findRestaurantByUserRating(String username) {
        List<Restaurant> restaurantsReviewedByUser = new ArrayList<>();
        var res = restaurantRepository.findAll();
        // check if user has left reviews for restaurants
        for (Restaurant restaurant : res) {
            if (restaurant.getReviews().stream().anyMatch(review -> review.getUser().getUsername().equals(username))) {
                restaurantsReviewedByUser.add(restaurant);
            }
        }

        // return the restaurant with the highest rating, if it is >= than 4.0
        if (restaurantsReviewedByUser.size() > 0) {
            var filteredRestaurants = restaurantsReviewedByUser.stream().filter(restaurant -> Double.parseDouble(restaurant.getRating()) >= 4.0).toList();
            if (filteredRestaurants.size() > 0) {
                return filteredRestaurants.stream().max(Comparator.comparing(Restaurant::getRating)).get();
            }
        }

        // if no restaurants have been reviewed by user with rating > 4.0, return the restaurant with the highest rating overall
        return res.stream().max(Comparator.comparing(Restaurant::getRating)).get();
    }

    public AllRestaurants findRestaurantsByCluster(String username) {
        // find user's favorite restaurant or a restaurant with the highest rating
        var favRestaurant = findRestaurantByUserRating(username);

        // find all restaurants in the same cluster as the user's favorite restaurant
        var cluster = favRestaurant.getCluster();
        var allRestaurantsFromSameCluster = new AllRestaurants();
        try {
            allRestaurantsFromSameCluster.setData(restaurantRepository.findByClusterOrderByRatingDesc(cluster));
            allRestaurantsFromSameCluster.setStatusCode(200);
        } catch (Exception e) {
            allRestaurantsFromSameCluster.setError("Restaurants not found");
            allRestaurantsFromSameCluster.setStatusCode(404);
        }

        for (var restaurant : allRestaurantsFromSameCluster.getData()) {
            System.out.println(restaurant.getName() + " " + restaurant.getCluster());
        }

        return allRestaurantsFromSameCluster;
    }

    public void deleteBooking(Long id) {
        var booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return;
        }

        var restaurant = booking.get().getRestaurant();
        restaurant.removeBooking(booking.get());

        bookingRepository.deleteById(id);

        restaurantRepository.save(restaurant);
    }

    public SingleRestaurant findRestaurantByManager(String username) {
        var restaurant = new SingleRestaurant();
        try {
            restaurant.setData(restaurantRepository.findByManagerUsername(username));
            restaurant.setStatusCode(200);
        } catch (Exception e) {
            restaurant.setError("Restaurant not found");
            restaurant.setStatusCode(404);
        }
        return restaurant;
    }

    public SingleRestaurant updateRestaurant(String username, RestaurantUpdateDTO updatedRestaurant) {
        var restaurantToUpdate = restaurantRepository.findByManagerUsername(username);

        if (restaurantToUpdate == null) {
            var singleRestaurant = new SingleRestaurant();
            singleRestaurant.setError("Restaurant not found");
            singleRestaurant.setStatusCode(404);
            return singleRestaurant;
        }
        if (updatedRestaurant.getTotalSeats() != null && updatedRestaurant.getTotalSeats() > 0) {
            restaurantToUpdate.setTotalSeats(updatedRestaurant.getTotalSeats());

            var bookings = bookingRepository.findByRestaurant(restaurantToUpdate);
            for (var booking : bookings) {
                booking.setTotalSeats(updatedRestaurant.getTotalSeats());
                bookingRepository.save(booking);
            }
        }
        if (updatedRestaurant.getName() != null && !updatedRestaurant.getName().equals("")) {
            restaurantToUpdate.setName(updatedRestaurant.getName());
        }
        if (updatedRestaurant.getDescription() != null && !updatedRestaurant.getDescription().equals("")) {
            restaurantToUpdate.setDescription(updatedRestaurant.getDescription());
        }
        if (updatedRestaurant.getPhone() != null && !updatedRestaurant.getPhone().equals("")) {
            restaurantToUpdate.setPhone(updatedRestaurant.getPhone());
        }
        if (updatedRestaurant.getAddress() != null && !updatedRestaurant.getAddress().equals("")) {
            restaurantToUpdate.setAddress(updatedRestaurant.getAddress());
        }
        return new SingleRestaurant(restaurantRepository.save(restaurantToUpdate), "", 200);
    }
}
