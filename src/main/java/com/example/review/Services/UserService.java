package com.example.review.Services;


import com.example.review.Classes.User;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/create")
    public ResponseEntity<?> createUser(String email, String password, String firstName, String lastName){
        if(userRepository.findById(email).isPresent()){
            userRepository.save(new User(email, passwordEncoder.encode(password), firstName, lastName));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeUser(String email, String password, String firstName, String lastName){
        Optional<User> userOpt=userRepository.findById(email);
        if(userOpt.isPresent()){
            User user=userOpt.get();
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser(String email,String password){
        Optional<User> userOpt=userRepository.findById(email);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> getUser(){
        userRepository.deleteById(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}