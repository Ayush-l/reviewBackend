package com.example.review.Controllers;


import com.example.review.Entity.AuthRequest;
import com.example.review.Entity.User;
import com.example.review.Services.JWTService;
import com.example.review.Services.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/create")
public class CreationController{
    private static final Log log = LogFactory.getLog(CreationController.class);
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Autowired
    CreationController(UserService userService,JWTService jwtService){
        this.userService=userService;
        passwordEncoder=new BCryptPasswordEncoder();
        this.jwtService=jwtService;
    }

    @PostMapping("/createuser")
    public ResponseEntity<Boolean> createUser(@RequestBody User user){
        if(userService.createUser(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(),user.getRole())) return new ResponseEntity<>(true, HttpStatus.CREATED);
        return new ResponseEntity<>(false,HttpStatus.ALREADY_REPORTED);
    }

    @PostMapping("/gettoken")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest user){
        User u=userService.getUser(user.getUsername());
        if(u==null || !passwordEncoder.matches(user.getPassword(),u.getPassword()) || !u.getRole().equals(user.getRole())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(jwtService.generateToken(user.getUsername()),HttpStatus.ACCEPTED);
    }
}
