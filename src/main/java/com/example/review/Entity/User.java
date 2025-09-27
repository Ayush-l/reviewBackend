package com.example.review.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@Document
@Component
@AllArgsConstructor
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

    public void deleteReview(Review review){
        reviews.remove(review);
    }
}