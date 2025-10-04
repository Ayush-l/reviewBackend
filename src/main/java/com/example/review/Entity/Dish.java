package com.example.review.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document
@Component
public class Dish{

    @Id
    private String name;


    @DBRef
    private Image image;

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
