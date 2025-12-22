package com.example.review.Services;

import com.example.review.Entity.Cafe;
import com.example.review.Entity.CafeToret;
import com.example.review.Entity.Review;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.CafeToRetRepository;
import com.example.review.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final CafeService cafeService;
    private final CafeRepository cafeRepository;
    private final CafeToRetRepository cafeToRetRepository;

    @Autowired
    public ReviewService(CafeToRetRepository cafeToRetRepository,ReviewRepository reviewRepository,UserService userService,CafeService cafeService,CafeRepository cafeRepository){
        this.reviewRepository=reviewRepository;
        this.cafeToRetRepository=cafeToRetRepository;
        this.userService=userService;
        this.cafeService=cafeService;
        this.cafeRepository=cafeRepository;
    }

    @Transactional
    public boolean addReview(String email,String id,String dish,String comment,int rating){
        Review review=new Review();
        review.setCafe(id);
        review.setDish(dish);
        review.setReview(comment);
        review.setUser(userService.getUser(email));
        review.setRating(rating);
        reviewRepository.save(review);
        Cafe cafe=cafeService.getCafe(id);
        cafe.addReview(rating,dish);
        cafeRepository.save(cafe);
        CafeToret cafeToret=cafeToRetRepository.findById(id).orElse(null);
        cafeToret.setRating(cafe.getCafeRating());
        cafeToRetRepository.save(cafeToret);
        return true;
    }
}
