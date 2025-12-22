package com.example.review.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Document
@Component
public class User {
    @Id
    private String email;

    private String password;
    private String firstName;
    private String lastName;

    private String role;


    @DBRef
    private List<Review> reviews;

    @DBRef
    private Cafe cafeAdded;

    @DBRef
    private Image image;

    public User() {
        email="";
        password="";
        firstName="";
        lastName="";
        role="";
        reviews=new ArrayList<>();
        cafeAdded=null;
        image=null;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public void deleteReview(Review review){
        reviews.remove(review);
    }

    public Image getImage() {
        return image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setCafeAdded(Cafe cafeAdded) {
        this.cafeAdded = cafeAdded;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Cafe getCafeAdded() {
        return cafeAdded;
    }

}