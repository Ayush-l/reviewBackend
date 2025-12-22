package com.example.review.Repositories;

import com.example.review.Entity.Dish;
import com.example.review.Entity.DishRev;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishRepository extends MongoRepository<Dish, String> {
}
