package com.example.review.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.round;


@Document
@Component
public class Cafe {

    @Id
    private String id= UUID.randomUUID().toString();

    private String name;

    private int dishesCount;

    private String address;

    private List<String> images;

    private List<DishRev> dishes;

    private double cafeRating;

    public void Cafe(){
        cafeRating=0;
        dishes=new ArrayList<>();
        images=new ArrayList<>();
        dishesCount=0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishesCount(int dishesCount) {
        this.dishesCount = dishesCount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setDishes(List<DishRev> dishes) {
        this.dishes = dishes;
    }

    public void setCafeRating(double cafeRating) {
        this.cafeRating = cafeRating;
    }

    public String getName() {
        return name;
    }

    public int getDishesCount() {
        return dishesCount;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getImages() {
        return images;
    }

    public List<DishRev> getDishes() {
        return dishes;
    }

    public double getCafeRating() {
        return cafeRating;
    }


    public void addDish(DishRev dish){
        if(isPresent(dish.getName())) return;
        dishes.add(dish);
        dishesCount++;
    }

    public boolean isPresent(String name){
        for(int i=0;i<dishesCount;i++){
            if(dishes.get(i).getName().equals(name)) return true;
        }
        return false;
    }



    public void addReview(int x,String dishName){
        double sum=0,count=0;
        for(int i=0;i<dishesCount;i++){
            if(dishes.get(i).getName().equals(dishName)) dishes.get(i).addRating(x);
            if(dishes.get(i).getRating() != null){
                sum+=dishes.get(i).getRating();
                count++;
            }
        }
        cafeRating=sum/count;
    }
}