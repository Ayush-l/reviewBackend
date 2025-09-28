package com.example.review.Services;


import com.example.review.Entity.Cafe;
import com.example.review.Entity.Review;
import com.example.review.Entity.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.ReviewRepository;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, CafeRepository cafeRepository){
        this.reviewRepository=reviewRepository;
        this.userRepository=userRepository;
        this.cafeRepository=cafeRepository;
    }

    public boolean addReview(String email,Review review,String dish,String id){
        Optional<User> optUser=userRepository.findById(email);
        if(optUser.isEmpty()) return false;
        Optional<Cafe> optCafe=cafeRepository.findById(id);
        if(optCafe.isEmpty()) return false;
        User user=optUser.get();
        Cafe cafe=optCafe.get();
        for(Review review1:user.getReviews()){
            if(Objects.equals(review1.getCafe().getId(), id) && review1.getDish().getName().equals(dish)) return false;
        }
        review.setUser(user);
        List<Review> r=user.getReviews();
        r.add(review);
        user.setReviews(r);
        userRepository.save(user);
        reviewRepository.save(review);
        cafe.addReview(review);
        cafeRepository.save(cafe);
        return true;
    }

    public boolean deleteReview(String id){
        Optional<Review> optReview=reviewRepository.findById(id);
        if(optReview.isPresent()){
            Review review=optReview.get();
            User user=review.getUser();
            user.deleteReview(review);
            Cafe cafe=review.getCafe();
            cafe.deleteReview(review);
            cafeRepository.save(cafe);
            userRepository.save(user);
            reviewRepository.deleteById(id);
        }
        return true;
    }


    public boolean changeReview(Review review){
        reviewRepository.save(review);
        return true;
    }
}