package com.example.review.Services;


import com.example.review.Entity.*;
import com.example.review.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final CafeToRetRepository cafeToretRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CafeService(CafeRepository cafeRepository, UserRepository userRepository, DishRepository dishRepository, ReviewRepository reviewRepository,ReviewService reviewService,CafeToRetRepository cafeToretRepository){
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeRepository=cafeRepository;
        this.userRepository=userRepository;
        this.dishRepository=dishRepository;
        this.reviewRepository=reviewRepository;
        this.reviewService=reviewService;
        this.cafeToretRepository=cafeToretRepository;
    }

    public Cafe getCafe(String id){
        Optional<Cafe> optCafe=cafeRepository.findById(id);
        if(optCafe.isEmpty()) return null;
        Cafe cafe=optCafe.get();
        int dCount=cafe.getDishesCount();
        List<List<Review>> lR=new ArrayList<>();
        List<List<Review>> reviews=cafe.getReviews();
        for(int i=0;i<dCount;i++) lR.add(reviews.get(i).subList(0,3));
        cafe.setReviews(lR);
        return cafe;
    }

    public List<Review> getCafeReviews(String id, String name,int from){
        Optional<Cafe> optCafe=cafeRepository.findById(id);
        if(optCafe.isEmpty()) return null;
        Cafe cafe=optCafe.get();
        int dCount=cafe.getDishesCount();
        List<Review> p=new ArrayList<>();
        List<List<Review>> reviews=cafe.getReviews();
        for(int i=0;i<dCount;i++){
            if(cafe.getDishes().get(i).getName().equals(name)){
                p=cafe.getReviews().get(i).subList(from,from+10);
                break;
            }
        }
        return p;
    }

    public boolean addReview(String id, Review review){
        Cafe cafe=getCafe(id);
        if(cafe==null) return false;
        cafe.addReview(review);
        cafeRepository.save(cafe);
        return true;
    }

    public boolean addDish(String name,String id){
        Cafe cafe=getCafe(id);
        if(cafe==null) return false;
        Optional<Dish> optDish=dishRepository.findById(name);
        if(optDish.isPresent()){
            cafe.addDish(optDish.get());
            cafeRepository.save(cafe);
        }
        return true;
    }

    public boolean deleteCafe(String email,String passWord){
        Optional<User> optUser=userRepository.findById(email);
        if(optUser.isEmpty()) return false;
        User user=optUser.get();
        if(passwordEncoder.matches(user.getPassword(),passWord)){
            java.util.Optional<Cafe> optCafe=cafeRepository.findById(user.getCafeAdded().getId());
            if(optCafe.isPresent()){
                Cafe cafe=optCafe.get();
                for(List<Review> r:cafe.getReviews()){
                    for(Review r1:r) reviewService.deleteReview(r1.getId());
                }
            }
            user.setCafeAdded(null);
            userRepository.save(user);
        }
        return false;
    }

    public Page<CafeToret> getAllCafe(Pageable pageable){
        return cafeToretRepository.findAll(pageable);
    }

    public long getTotalPages(){
        return cafeRepository.count();
    }
}
