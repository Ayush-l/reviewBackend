package com.example.review.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.lang.Math.round;


@AllArgsConstructor
@Document
@Data
@NoArgsConstructor
@Component
public class Cafe {

    @Id
    private String id= UUID.randomUUID().toString();;

    private String name;

    private int dishesCount;

    @DBRef
    private User user;

    @DBRef
    private List<Dish> dishes;

    private List<Double> rating;
    private List<Integer> ratingCount;

    private int cafeRating;

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