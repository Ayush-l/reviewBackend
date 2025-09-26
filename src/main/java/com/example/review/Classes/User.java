package com.example.review.Classes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

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
}