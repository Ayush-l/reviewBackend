package com.example.review.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Document
@Component
public class Image {

    @Id
    private String id= UUID.randomUUID().toString();
    private String fileType;
    private byte[] file;

    public void setUser(User user) {
        this.user = user;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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

    @DBRef
    private User user;

    @DBRef
    private Cafe cafe;

    @DBRef
    private Dish dish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }


    public byte[] getFile() {
        return file;
    }

}
