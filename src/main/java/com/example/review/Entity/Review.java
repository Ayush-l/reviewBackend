package com.example.review.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Document
@Component
public class Review{

    @Id
    private String id= UUID.randomUUID().toString();;

    public void setId(String id) {
        this.id = id;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReview() {
        return review;
    }

    public User getUser() {
        return user;
    }

    public Cafe getCafe() {
        return cafe;
    }

    public Dish getDish() {
        return dish;
    }

    public int getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    private String review;
    private int rating;

    @DBRef
    private Dish dish;

    @DBRef
    private Cafe cafe;

    @DBRef
    private User user;
}
