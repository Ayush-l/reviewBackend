package com.example.review.Controllers;


import com.example.review.Entity.Body;
import com.example.review.Entity.Review;
import com.example.review.FIlter.JwtAuthFilter;
import com.example.review.Services.JWTService;
import com.example.review.Services.ReviewService;
import com.google.api.Http;
import com.google.api.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/review")
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JWTService jwtService;

    @Autowired
    public ReviewController(ReviewService reviewService,JwtAuthFilter jwtAuthFilter,JWTService jwtService){
        this.reviewService=reviewService;
        this.jwtAuthFilter=jwtAuthFilter;
        this.jwtService=jwtService;
    }

    @PostMapping("/verifyUser")
    public ResponseEntity<Void> verifyUser(@RequestBody Body body){
        try{
            if(jwtAuthFilter.doFilterInternal(body.getAuthToken(), "user")){
                if(reviewService.verifyUser(jwtService.extractUsername(body.getAuthToken().substring(7)),body.getCafe().getId(),body.getDish().getName())) return new ResponseEntity<>(HttpStatus.OK);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/addReview")
    public ResponseEntity<Boolean> addReview(@RequestBody Body body){
        try{
            if(jwtAuthFilter.doFilterInternal(body.getAuthToken(), "user")){
                reviewService.addReview(jwtService.extractUsername(body.getAuthToken().substring(7)),body.getCafe().getId(),body.getDish().getName(),body.getReview().getReview(),body.getReview().getRating());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}