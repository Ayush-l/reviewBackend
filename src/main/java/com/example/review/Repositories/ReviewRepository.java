package com.example.review.Repositories;

import com.example.review.Entity.Review;
import com.example.review.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    Page<Review> findByCafeAndDish(
            String cafe,
            String dish,
            Pageable pageable
    );
    boolean existsByUserAndCafeIdAndDish(
            User user,
            String cafeId,
            String dish
    );
}
