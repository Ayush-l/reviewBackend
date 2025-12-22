package com.example.review.Controllers;

import com.example.review.Entity.Cafe;
import com.example.review.Entity.CafeToret;
import com.example.review.Entity.Review;
import com.example.review.Services.CafeService;
import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/getcafe")
public class CafeGettersController {

    @Autowired
    private CafeService cafeService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Cafe> getCafe(@PathVariable String id){
        Cafe cafe=cafeService.getCafe(id);
        return new ResponseEntity<>(cafe, HttpStatus.OK);
    }


    @GetMapping("/getall/{p}")
    public ResponseEntity<Page<CafeToret>> getCafes(@PathVariable int p){
        Page<CafeToret> page=cafeService.getAllCafe(PageRequest.of(p-1,30, Sort.by("rating").descending()));
        if(!page.isEmpty()) return new ResponseEntity<>(page, HttpStatus.OK);
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search/{prefix}/{page}")
    public ResponseEntity<Page<CafeToret>> search(@PathVariable String prefix,@PathVariable int page){
        return new ResponseEntity<>(cafeService.searchPrefix(prefix,page), HttpStatus.OK);
    }

    @GetMapping("/reviews/{dishName}/{id}/{page}")
    public ResponseEntity<Page<Review>> getReviews(@PathVariable int page, @PathVariable String id, @PathVariable String dishName){
        return new ResponseEntity<>(cafeService.getCafeReviews(id,dishName,page),HttpStatus.OK);
    }

}
