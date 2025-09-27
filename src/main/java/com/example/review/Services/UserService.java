package com.example.review.Services;


import com.example.review.Classes.Cafe;
import com.example.review.Classes.Review;
import com.example.review.Classes.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.ReviewRepository;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CafeRepository cafeRepository;
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final CafeService cafeService;
    @Autowired
    private final ReviewService reviewService;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CafeRepository cafeRepository,ReviewRepository reviewRepository,CafeService cafeService,ReviewService reviewService){
        this.userRepository=userRepository;
        this.cafeRepository=cafeRepository;
        this.reviewRepository=reviewRepository;
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeService=cafeService;
        this.reviewService=reviewService;
    }

    public boolean createUser(String email, String password, String firstName, String lastName,String role){
        if(getUser(email)==null){
            User user=new User();
            user.setEmail(email);
            user.setRole(role.toLowerCase());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public boolean changeUser(String email, String password, String firstName, String lastName){
        User user=getUser(email);
        if(user!=null){
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

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

    public boolean addCafe(String email,String name){
        User user=getUser(email);
        if(user!=null&&user.getCafeAdded()==null){
            Cafe cafe=new Cafe();
            cafe.setName(name);
            cafeRepository.save(cafe);
            user.setCafeAdded(cafe);
            userRepository.save(user);
            return true;
        }
        return false;
    }


}