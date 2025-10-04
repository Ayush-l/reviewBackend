package com.example.review.Services;


import com.example.review.Entity.Cafe;
import com.example.review.Entity.CafeToret;
import com.example.review.Entity.Review;
import com.example.review.Entity.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.CafeToRetRepository;
import com.example.review.Repositories.ReviewRepository;
import com.example.review.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final CafeService cafeService;
    private final ReviewService reviewService;
    private final CafeToRetRepository cafeToRetRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired
    public UserService(ImageService imageService,CafeToRetRepository cafeToRetRepository,UserRepository userRepository, CafeRepository cafeRepository,ReviewRepository reviewRepository,CafeService cafeService,ReviewService reviewService){
        this.userRepository=userRepository;
        this.cafeRepository=cafeRepository;
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeService=cafeService;
        this.reviewService=reviewService;
        this.cafeToRetRepository=cafeToRetRepository;
        this.imageService=imageService;
    }

    public boolean createUser(String email, String password, String firstName, String lastName,String role){
        if(getUser(email)==null){
            User user=new User();
            user.setEmail(email);
            user.setRole(role.toLowerCase());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setReviews(new ArrayList<>());
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public boolean changeUser(String email, String password, String firstName, String lastName){
        User user=getUser(email);
        if(user!=null){
            if(!passwordEncoder.matches(password,passwordEncoder.encode(password))) user.setPassword(passwordEncoder.encode(password));
            if(firstName!=null) user.setFirstName(firstName);
            if(lastName!=null) user.setLastName(lastName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean changeDp(String email,MultipartFile file){
        User user=getUser(email);
        if(user.getImage()!=null) imageService.deleteImage(user.getImage().getId());
        try{
            String id=imageService.addFile(file);
            user.setImage(imageService.loadFile(id));
            userRepository.save(user);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteUser(String email,String passWord){
        User user=getUser(email);
        if(user!=null&&passwordEncoder.matches(user.getPassword(),passWord)){
            for(Review review:user.getReviews()) reviewService.deleteReview(review.getId());
            if(user.getCafeAdded()!=null) cafeService.deleteCafe(email,passWord);
            userRepository.deleteById(email);
            return true;
        }
        return false;
    }


    public User getUser(String email){
        Optional<User> optUser=userRepository.findById(email);
        return optUser.orElse(null);
    }

    @Transactional
    public boolean addCafe(String email,String name){
        User user=getUser(email);
        if(user!=null&&user.getRole().equals("Owner")&&user.getCafeAdded()==null){
            Cafe cafe=new Cafe();
            cafe.setName(name);
            cafeRepository.save(cafe);
            user.setCafeAdded(cafe);
            userRepository.save(user);
            user=userRepository.findById(email).get();
            CafeToret cafeToret=new CafeToret();
            cafeToret.setName(user.getCafeAdded().getId());
            cafeToret.setImages(cafe.getImages());
            cafeToret.setRating(cafeToret.getRating());
            cafeToRetRepository.save(cafeToret);
            return true;
        }
        return false;
    }


}