package com.example.review.Entity;

import java.util.List;

public class ReviewsToret {
    List<Review> reviews;
    Dish dish;
    Double ratings;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public Dish getDish() {
        return dish;
    }

    public Double getRatings() {
        return ratings;
    }
}
