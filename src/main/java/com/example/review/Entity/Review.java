package com.example.review.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Review{

    @Id
    private String id;
    private int rating;
    private String review;
    private String cafe;
    private String dish;
    @DBRef
    private User user;

    private LocalDateTime creationTime;

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }



    public void setCafe(String cafe) {
        this.cafe = cafe;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getCafe() {
        return cafe;
    }

    public String getDish() {
        return dish;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

}
