package com.example.review.Repositories;

import com.example.review.Classes.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {
}
