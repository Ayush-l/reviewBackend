package com.example.review.Repositories;

import com.example.review.Entity.CafeToret;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;

public interface CafeToRetRepository extends MongoRepository<CafeToret,String> {
    Page<CafeToret> findByNameStartingWithIgnoreCase(String prefix, Pageable pageable);
}