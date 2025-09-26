package com.example.review.Classes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Document
@AllArgsConstructor
@Component
public class Review{

    @Id
    private String id;

    private String review;
    private int rating;

    @DBRef
    private Dish dish;

    @DBRef
    private User user;
}
