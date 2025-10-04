package com.example.review.Services;


import com.example.review.Entity.*;
import com.example.review.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CafeService {

    private static final Logger log = LoggerFactory.getLogger(CafeService.class);
    private final int MAXQUANTITYOFIMAGES;

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final CafeToRetRepository cafeToretRepository;
    private final ImageService imageService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CafeService(ImageService imageService,CafeRepository cafeRepository, UserRepository userRepository, DishRepository dishRepository, ReviewRepository reviewRepository,ReviewService reviewService,CafeToRetRepository cafeToretRepository){
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeRepository=cafeRepository;
        this.imageService=imageService;
        this.userRepository=userRepository;
        this.dishRepository=dishRepository;
        this.reviewRepository=reviewRepository;
        this.reviewService=reviewService;
        this.cafeToretRepository=cafeToretRepository;
        MAXQUANTITYOFIMAGES=10;
    }

    public Cafe getCafe(String id){
        Optional<Cafe> optCafe=cafeRepository.findById(id);
        return optCafe.orElse(null);
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

    public boolean addImage(String cafeId, MultipartFile file){
        try{
            Cafe cafe=getCafe(cafeId);
            if(cafe.getImages().size()==MAXQUANTITYOFIMAGES) return false;
            String id=imageService.addFile(file);
            cafe.getImages().add(imageService.loadFile(id));
            cafeRepository.save(cafe);
            return true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean deleteImage(String id,String cafeId){
        try{
            Cafe cafe=getCafe(cafeId);
            cafe.getImages().removeIf(x->x.getId().equals(id));
            cafeRepository.save(cafe);
            imageService.deleteImage(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }


    @Transactional
    public void deleteCafe(String email,String passWord){
        Optional<User> optUser=userRepository.findById(email);
        if(optUser.isEmpty()) return;
        User user=optUser.get();
        if(passwordEncoder.matches(user.getPassword(),passWord)){
            java.util.Optional<Cafe> optCafe=cafeRepository.findById(user.getCafeAdded().getId());
            if(optCafe.isPresent()){
                Cafe cafe=optCafe.get();
                for(List<Review> r:cafe.getReviews()){
                    for(Review r1:r) reviewService.deleteReview(r1.getId());
                }
                cafeRepository.deleteById(cafe.getId());
                cafeToretRepository.deleteById(cafe.getId());
            }
            user.setCafeAdded(null);
            userRepository.save(user);
        }
    }

    public Page<CafeToret> getAllCafe(Pageable pageable){
        return cafeToretRepository.findAll(pageable);
    }

    public long getTotalPages(){
        return (cafeRepository.count()+29)/30;
    }
}
