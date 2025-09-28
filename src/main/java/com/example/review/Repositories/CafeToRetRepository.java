package com.example.review.Repositories;

import com.example.review.Entity.CafeToret;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeToRetRepository extends MongoRepository<CafeToret,String> {
}
