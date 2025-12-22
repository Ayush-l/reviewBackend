package com.example.review.Services;


import com.example.review.Entity.*;
import com.example.review.Repositories.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CafeService {

    private static final Log log = LogFactory.getLog(CafeService.class);
    private final int MAXQUANTITYOFIMAGES;

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final CafeToRetRepository cafeToretRepository;
    private final ImageService imageService;
    private final ReviewRepository reviewRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CafeService(ReviewRepository reviewRepository, ImageService imageService,CafeRepository cafeRepository, UserRepository userRepository, DishRepository dishRepository,CafeToRetRepository cafeToretRepository){
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeRepository=cafeRepository;
        this.imageService=imageService;
        this.userRepository=userRepository;
        this.dishRepository=dishRepository;
        this.cafeToretRepository=cafeToretRepository;
        this.reviewRepository=reviewRepository;
        MAXQUANTITYOFIMAGES=10;
    }


    public Cafe getCafe(String id){
        Optional<Cafe> optCafe=cafeRepository.findById(id);
        return optCafe.orElse(null);
    }

    public Page<Review> getCafeReviews(String cafeId, String dish,int page){
        Pageable p= PageRequest.of(page,30, Sort.by(Sort.Direction.DESC,"creationTime"));
        return reviewRepository.findByCafeAndDish(cafeId,dish,p);
    }


    public boolean addDish(String name,String id){
        Cafe cafe=getCafe(id);
        if(cafe==null) return false;
        Optional<Dish> optDish=dishRepository.findById(name);
        if(optDish.isPresent()){
            Dish dish=optDish.get();
            DishRev dishRev=new DishRev();
            dishRev.setUrl(dish.getUrl());
            dishRev.setName(dish.getName());
            cafe.addDish(dishRev);
            try{
                cafeRepository.save(cafe);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    public boolean addImage(String cafeId, String url){
        try{
            Cafe cafe=getCafe(cafeId);
            if(cafe.getImages().size()==MAXQUANTITYOFIMAGES) return false;
            cafe.getImages().add(url);
            if(cafe.getImages().size()==1){
                CafeToret cafeToret=cafeToretRepository.findById(cafeId).get();
                cafeToret.setImage(cafe.getImages().get(0));
                cafeToretRepository.save(cafeToret);
            }
            cafeRepository.save(cafe);
            return true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean deleteImage(String url,String cafeId){
        try{
            Cafe cafe=getCafe(cafeId);
            if(!cafe.getImages().isEmpty() && cafe.getImages().get(0).equals(url)){
                CafeToret cafeToret=cafeToretRepository.findById(cafeId).get();
                if(cafe.getImages().size()>1) cafeToret.setImage(cafe.getImages().get(1));
                cafeToretRepository.save(cafeToret);
            }
            cafe.getImages().removeIf(x->x.equals(url));
            cafeRepository.save(cafe);
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
            Optional<Cafe> optCafe=cafeRepository.findById(user.getCafeAdded().getId());
            if(optCafe.isPresent()){
                Cafe cafe=optCafe.get();
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

    @Transactional
    public void changeName(String id,String name){
        Cafe cafe=getCafe(id);
        CafeToret cafeToret=cafeToretRepository.findById(cafe.getId()).orElse(null);
        cafeToret.setName(name);
        cafe.setName(name);
        cafeRepository.save(cafe);
        cafeToretRepository.save(cafeToret);
    }
}
