package com.example.review.Controllers;


import com.example.review.Entity.Body;
import com.example.review.Entity.Cafe;
import com.example.review.Entity.ReviewsToret;
import com.example.review.Entity.User;
import com.example.review.FIlter.JwtAuthFilter;
import com.example.review.Services.CafeService;
import com.example.review.Services.JWTService;
import com.example.review.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth/cafe")
public class CafeController{

    private final String ROLE="owner";
    private final UserService userService;
    private final CafeService cafeService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JWTService jwtService;


    @Autowired
    public CafeController(UserService userService,CafeService cafeService,JwtAuthFilter jwtAuthFilter,JWTService jwtService){
        this.cafeService=cafeService;
        this.userService=userService;
        this.jwtAuthFilter=jwtAuthFilter;
        this.jwtService=jwtService;
    }

//    @GetMapping("/getReviews/{id}")
//    public ResponseEntity<ArrayList<ReviewsToret>> getReviews(@PathVariable String id){
//        System.out.println(id);
//        return new ResponseEntity<>(cafeService.getCafeReviews(id),HttpStatus.OK);
//    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createCafe(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)){
                throw new Exception("Forbidden");
            }
            if(userService.addCafe(jwtService.extractUsername(body.getAuthToken().substring(7)),body.getCafe().getName(),body.getCafe().getAddress())) return new ResponseEntity<>(true,HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/getCafe")
    public ResponseEntity<Cafe> getCafe(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)){
                throw new Exception("Forbidden");
            }
            return new ResponseEntity<>(cafeService.getCafe(userService.getUser(jwtService.extractUsername(body.getAuthToken().substring(7))).getCafeAdded().getId()),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/adddish")
    public ResponseEntity<Boolean> addDish(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw new Exception("Forbidden");
            String name=body.getDish().getName();
            User user=userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if(user!=null && user.getCafeAdded()!=null && cafeService.addDish(name,user.getCafeAdded().getId())) return new ResponseEntity<>(true,HttpStatus.OK);
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/image/{url}/{cafeId}")
    public ResponseEntity<Void> deleteImage(@RequestBody Body body,@PathVariable String url,@PathVariable String cafeId) {
        try {
            if (!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw new Exception("Forbidden");
            cafeService.deleteImage(url, cafeId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/image/{url}")
    public ResponseEntity<Boolean> uploadImage(@RequestBody Body body,@PathVariable String url){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw new Exception("Forbidden");
            if(cafeService.addImage(body.getCafe().getId(),url)) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
            return new ResponseEntity<>(false,HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/updateName")
    public ResponseEntity<Boolean> changeName(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw new Exception("Forbidden");
            cafeService.changeName(body.getCafe().getId(),body.getCafe().getName());
            return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}