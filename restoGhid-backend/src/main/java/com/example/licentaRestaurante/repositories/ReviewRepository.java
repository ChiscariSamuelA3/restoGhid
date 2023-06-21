package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Restaurant;
import com.example.licentaRestaurante.entities.Review;
import com.example.licentaRestaurante.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser(User user);

    List<Review> findAllByRestaurant(Restaurant restaurant);

    Review findByUserAndRestaurant(User user, Restaurant restaurant);
}
