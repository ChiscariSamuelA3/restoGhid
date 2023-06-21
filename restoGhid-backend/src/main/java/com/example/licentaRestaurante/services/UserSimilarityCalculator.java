package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSimilarityCalculator {
    private final ReviewService reviewService;

    @Autowired
    public UserSimilarityCalculator(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public double calculateSimilarity(String username1, String username2) {
        var reviews1 = reviewService.getReviewsByUsername(username1);
        var reviews2 = reviewService.getReviewsByUsername(username2);

        if (reviews1.getData().isEmpty() || reviews2.getData().isEmpty()) {
            return 0;
        }

        // get the common restaurants
        List<Review> commonReviews1 = new ArrayList<>();
        List<Review> commonReviews2 = new ArrayList<>();
        for (Review r1 : reviews1.getData()) {
            for (Review r2 : reviews2.getData()) {
                if (r1.getRestaurant().equals(r2.getRestaurant())) {
                    commonReviews1.add(r1);
                    commonReviews2.add(r2);
                    break;
                }
            }
        }

        int n = commonReviews1.size();
        // if there are no common restaurants, return 0
        if (n == 0) {
            return 0;
        }

        // calculate the sum of the ratings
        double sum1 = 0;
        double sum2 = 0;

        for (int i = 0; i < n; i++) {
            sum1 += commonReviews1.get(i).getScore();
            sum2 += commonReviews2.get(i).getScore();
        }

        // calculate the squares of the ratings
        double sum1Sq = 0;
        double sum2Sq = 0;

        for (int i = 0; i < n; i++) {
            sum1Sq += Math.pow(commonReviews1.get(i).getScore(), 2);
            sum2Sq += Math.pow(commonReviews2.get(i).getScore(), 2);
        }

        // calculate the sum of the products
        double pSum = 0;

        for (int i = 0; i < n; i++) {
            pSum += commonReviews1.get(i).getScore() * commonReviews2.get(i).getScore();
        }

        // calculate the Pearson score
        double num = pSum - (sum1 * sum2 / n);
        double den = Math.sqrt((sum1Sq - Math.pow(sum1, 2) / n) * (sum2Sq - Math.pow(sum2, 2) / n));
        if (den == 0) {
            return 0;
        }

        return num / den;
    }
}
