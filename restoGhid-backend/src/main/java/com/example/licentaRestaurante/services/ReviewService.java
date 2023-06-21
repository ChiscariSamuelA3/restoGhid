package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Review;
import com.example.licentaRestaurante.models.reviews.AllReviews;
import com.example.licentaRestaurante.models.reviews.SingleReview;
import com.example.licentaRestaurante.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService, RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    public AllReviews getAllReviews() {
        var reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
            return new AllReviews(new ArrayList<>(), "No reviews found", 404);
        }

        return new AllReviews(reviews, "", 200);
    }

    public AllReviews getReviewsByUsername(String username) {
        var user = userService.getUserByUsername(username);

        if (user.getData() == null) {
            return new AllReviews(new ArrayList<>(), "User not found", 404);
        }

        var reviews = reviewRepository.findAllByUser(user.getData());

        if (reviews.isEmpty()) {
            return new AllReviews(new ArrayList<>(), "No reviews found", 404);
        }

        return new AllReviews(reviews, "", 200);
    }

    public AllReviews getReviewsByRestaurantId(String id) {
        var restaurant = restaurantService.findRestaurantById(id);

        if (restaurant.getData() == null) {
            return new AllReviews(new ArrayList<>(), "Restaurant not found", 404);
        }

        var reviews = reviewRepository.findAllByRestaurant(restaurant.getData());

        if (reviews.isEmpty()) {
            return new AllReviews(new ArrayList<>(), "No reviews found", 404);
        }

        return new AllReviews(reviews, "", 200);
    }

    public SingleReview addReview(String username, String restaurantId, Review review) {
        var user = userService.getUserByUsername(username);

        if (user.getData() == null) {
            return new SingleReview(null, "User not found", 404);
        }

        var restaurant = restaurantService.findRestaurantById(restaurantId);

        if (restaurant.getData() == null) {
            return new SingleReview(null, "Restaurant not found", 404);
        }

        // check if user already reviewed this restaurant
        var reviewToUpdate = reviewRepository.findByUserAndRestaurant(user.getData(), restaurant.getData());

        if (reviewToUpdate != null) {
            reviewToUpdate.setContent(review.getContent());
            reviewToUpdate.setScore(review.getScore());
            reviewToUpdate.setDate(java.time.LocalDate.now());

            reviewRepository.save(reviewToUpdate);

            return new SingleReview(reviewToUpdate, "", 200);
        }

        var newReview = new Review();
        newReview.setUsername(user.getData().getUsername());
        newReview.setIdUser(user.getData().getId());
        newReview.setIdRestaurant(restaurantId);
        newReview.setRestaurant(restaurant.getData());
        newReview.setUser(user.getData());
        newReview.setContent(review.getContent());
        newReview.setScore(review.getScore());
        newReview.setDate(java.time.LocalDate.now());

        reviewRepository.save(newReview);

        return new SingleReview(newReview, "", 200);
    }

    public AllReviews getReviewsByManager(String username) {
        var restaurant = restaurantService.findRestaurantByManager(username);

        if (restaurant.getData() == null) {
            return new AllReviews(new ArrayList<>(), "No restaurants found", 404);
        }

        var reviews = reviewRepository.findAllByRestaurant(restaurant.getData());

        if (reviews.isEmpty()) {
            return new AllReviews(new ArrayList<>(), "No reviews found", 404);
        }

        return new AllReviews(reviews, "", 200);
    }

    public SingleReview replyToReview(Long id, String reply) {
        var reviewToUpdate = reviewRepository.findById(id);

        if (reviewToUpdate.isEmpty()) {
            return new SingleReview(null, "Review not found", 404);
        }

        var reviewUpdated = reviewToUpdate.get();
        reviewUpdated.setReply(reply);

        reviewRepository.save(reviewUpdated);

        return new SingleReview(reviewUpdated, "", 200);
    }
}
