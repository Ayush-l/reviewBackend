package com.example.review.Services;


import com.example.review.Classes.Review;
import com.example.review.Classes.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.ReviewRepository;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private CafeService cafeService;
    @Autowired
    private ReviewRepository reviewRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean createUser(String email, String password, String firstName, String lastName,String role){
        if(getUser(email).equals(null)){
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
        if(!user.equals(null)){
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
        if(!user.equals(null)&&passwordEncoder.matches(user.getPassword(),passWord)){
            if(!user.getCafeAdded().equals(null)) cafeRepository.deleteById(user.getCafeAdded().getId());
            for(Review review:user.getReviews()) reviewRepository.deleteById(review.getId());
            userRepository.deleteById(email);
            return true;
        }
        return false;
    }


    public User getUser(String email){
        return userRepository.findById(email).get();
    }

    public boolean addCafe(Long id,String email){
        User user=getUser(email);
        if(!user.equals(null)&&user.getCafeAdded().equals(null)){
            user.setCafeAdded(cafeRepository.findById(id).get());
            userRepository.save(user);
            return true;
        }
        return false;
    }


}