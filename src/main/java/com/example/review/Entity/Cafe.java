package com.example.review.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @DBRef
    List<Image> images;

    public void setId(String id) {
        this.id = id;
    }

    public void setReviews(List<List<Review>> reviews) {
        this.reviews = reviews;
    }

    public void setCafeRating(double cafeRating) {
        this.cafeRating = cafeRating;
    }

    public void setRatingCount(List<Integer> ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setRating(List<Double> rating) {
        this.rating = rating;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDishesCount(int dishesCount) {
        this.dishesCount = dishesCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDishesCount() {
        return dishesCount;
    }

    public User getUser() {
        return user;
    }

    public List<Double> getRating() {
        return rating;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public List<Integer> getRatingCount() {
        return ratingCount;
    }

    public double getCafeRating() {
        return cafeRating;
    }

    public List<List<Review>> getReviews() {
        return reviews;
    }

    @DBRef
    private User user;

    @DBRef
    private List<Dish> dishes;

    private List<Double> rating;
    private List<Integer> ratingCount;

    private double cafeRating;

    @DBRef
    private List<List<Review>> reviews;

    public void addDish(Dish dish){
        if(isPresent(dish.getName())) return;
        dishes.add(dish);
        rating.add(0D);
        ratingCount.add(0);
        dishesCount++;
    }

    public boolean isPresent(String name){
        for(int i=0;i<dishesCount;i++){
            if(dishes.get(i).getName().equals(name)) return true;
        }
        return false;
    }

    public void setRatings(){
        int c=0;
        double r=0;
        for(int i=0;i<dishesCount;i++){
            if(rating.get(i)>1){
                r+=rating.get(i);
                c++;
            }
        }
        cafeRating=(int)round(c/r);
    }
    public void addReview(Review review){
        if(!isPresent(review.getDish().getName())) return;
        for(int i=0;i<dishesCount;i++){
            if(review.getDish().getName().equals(dishes.get(i).getName())){
                List<Review> r=reviews.get(i);
                r.add(review);
                reviews.set(i,r);
                rating.set(i,(rating.get(i)*ratingCount.get(i)+review.getRating())/(ratingCount.get(i)+1));
                ratingCount.set(i,ratingCount.get(i)+1);
                break;
            }
        }

    }

    public void deleteReview(Review review){
        for(int i=0;i<dishesCount;i++){
            if(dishes.get(i).getName().equals(review.getDish().getName())){
                List<Review> r=reviews.get(i);
                r.remove(review);
                reviews.set(i,r);
                if(ratingCount.get(i)>1) rating.set(i,(rating.get(i)*ratingCount.get(i)-review.getRating())/(ratingCount.get(i)-1));
                else rating.set(i,0D);
                ratingCount.set(i,ratingCount.get(i)-1);
                break;
            }
        }
    }
}