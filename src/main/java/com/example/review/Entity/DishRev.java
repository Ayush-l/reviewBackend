package com.example.review.Entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;


public class DishRev {
    private String name;

    private String url;
    private List<Integer> ratingCount;

    private Double rating;

    public List<Integer> getRatingCount() {
        return ratingCount;
    }

    public void addRating(int x){
        ratingCount.set(x-1,ratingCount.get(x-1)+1);
        double sum=0,count=0;
        for(int i=0;i<5;i++){
            count+=ratingCount.get(i);
            sum+=(i+1)*ratingCount.get(i);
        }
        rating=sum/count;
    }

    public void setRatingCount(List<Integer> ratingCount) {
        this.ratingCount = ratingCount;
    }

    public DishRev(){
        ratingCount=new ArrayList<>();
        for(int i=0;i<5;i++) ratingCount.add(0);
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
