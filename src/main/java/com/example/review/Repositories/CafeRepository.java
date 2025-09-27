package com.example.review.Repositories;

import com.example.review.Entity.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {
}
