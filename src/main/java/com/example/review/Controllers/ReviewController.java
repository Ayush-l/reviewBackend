package com.example.review.Controllers;


import com.example.review.Entity.Review;
import com.example.review.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController{
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService=reviewService;
    }

    @PostMapping("/create/{cafeId}/{dish}")
    public ResponseEntity<Boolean> createReview(@PathVariable String cafeId,@PathVariable String dish,@RequestBody Review review){
        if(reviewService.addReview(SecurityContextHolder.getContext().getAuthentication().getName(),review,dish,cafeId)) return new ResponseEntity<>(true, HttpStatus.CREATED);
        return new ResponseEntity<>(false,HttpStatus.ALREADY_REPORTED);
    }

    @PutMapping("/change")
    public ResponseEntity<Boolean> changeReview(@RequestBody Review newReview){
        if(reviewService.changeReview(newReview)) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false,HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable String id){
        if(reviewService.deleteReview(id)) return new ResponseEntity<Boolean>(true,HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false,HttpStatus.BAD_GATEWAY);
    }
}