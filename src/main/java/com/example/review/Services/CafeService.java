package com.example.review.Services;


import com.example.review.Classes.Cafe;
import com.example.review.Classes.Dish;
import com.example.review.Classes.Review;
import com.example.review.Classes.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.DishRepository;
import com.example.review.Repositories.ReviewRepository;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CafeService {

    @Autowired
    private final CafeRepository cafeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final DishRepository dishRepository;
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final ReviewService reviewService;

    private final BCryptPasswordEncoder passwordEncoder;

    public CafeService(CafeRepository cafeRepository, UserRepository userRepository, DishRepository dishRepository, ReviewRepository reviewRepository,ReviewService reviewService){
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeRepository=cafeRepository;
        this.userRepository=userRepository;
        this.dishRepository=dishRepository;
        this.reviewRepository=reviewRepository;
        this.reviewService=reviewService;
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

    public boolean createCafe(String id){
        if(getCafe(id)==null){
            Cafe cafe=new Cafe();
            cafe.setId(id);
            cafe.setDishesCount(0);
            cafe.setCafeRating(0);
            cafe.setCafeRating(0);
            cafeRepository.save(cafe);
            return true;
        }
        return false;
    }

    public boolean addReview(String id, Review review){
        Cafe cafe=getCafe(id);
        if(cafe==null) return false;
        cafe.addReview(review);
        cafeRepository.save(cafe);
        return true;
    }

    public boolean addDish(String name,String url,String id){
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
}
