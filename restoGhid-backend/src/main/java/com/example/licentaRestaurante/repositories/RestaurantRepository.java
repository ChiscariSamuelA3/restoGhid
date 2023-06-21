package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Cuisine;
import com.example.licentaRestaurante.entities.DietaryRestriction;
import com.example.licentaRestaurante.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Restaurant findByLocationId(String location_id);
    List<Restaurant> findByClusterOrderByRatingDesc(int cluster);
    Restaurant findByManagerUsername(String username);
}
