package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.entities.Review;
import com.example.licentaRestaurante.models.reviews.AllReviews;
import com.example.licentaRestaurante.models.reviews.SingleReview;
import com.example.licentaRestaurante.services.ReviewService;
import com.example.licentaRestaurante.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ReviewController(ReviewService reviewService, JwtUtils jwtUtils) {
        this.reviewService = reviewService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public AllReviews getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping("/add/{restaurantId}")
    public SingleReview addReview(@RequestHeader("Authorization") String token, @PathVariable String restaurantId, @RequestBody Review review) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return reviewService.addReview(username, restaurantId, review);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public AllReviews getReviewsByRestaurantId(@PathVariable String restaurantId) {
        return reviewService.getReviewsByRestaurantId(restaurantId);
    }

    @GetMapping("/restaurant/manager")
    public AllReviews getReviewsByManager(@RequestHeader("Authorization") String token) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return reviewService.getReviewsByManager(username);
    }

    @PatchMapping("/reply/{reviewId}")
    public SingleReview replyToReview(@PathVariable Long reviewId, @RequestBody String reply) {
        return reviewService.replyToReview(reviewId, reply);
    }
}
