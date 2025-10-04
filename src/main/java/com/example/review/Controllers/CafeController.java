package com.example.review.Controllers;


import com.example.review.Entity.Cafe;
import com.example.review.Entity.CafeToret;
import com.example.review.Entity.User;
import com.example.review.Services.CafeService;
import com.example.review.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth/cafe")
public class CafeController{
    private final UserService userService;
    private final CafeService cafeService;


    @Autowired
    public CafeController(UserService userService,CafeService cafeService){
        this.cafeService=cafeService;
        this.userService=userService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Cafe> getCafe(@PathVariable String id){
        return new ResponseEntity<>(cafeService.getCafe(id),HttpStatus.OK);
    }

    @GetMapping("/getpagesno")
    public ResponseEntity<Long> getNoPages(){
        return new ResponseEntity<>(cafeService.getTotalPages(),HttpStatus.OK);
    }

    @GetMapping("/getall/{page}")
    public ResponseEntity<Page<CafeToret>> getCafe(@PathVariable int p){
        Page<CafeToret> page=cafeService.getAllCafe(PageRequest.of(p,30, Sort.by("rating").descending()));
        if(!page.isEmpty()) return new ResponseEntity<>(page, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createCafe(@RequestBody Cafe cafe){
        if(userService.addCafe(SecurityContextHolder.getContext().getAuthentication().getName(),cafe.getName())) return new ResponseEntity<>(true,HttpStatus.OK);
        return new ResponseEntity<>(false,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping("/adddish")
    public ResponseEntity<Boolean> addDish(@RequestBody String name){
        User user=userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user!=null && user.getCafeAdded()!=null && cafeService.addDish(name,user.getCafeAdded().getId())) return new ResponseEntity<>(true,HttpStatus.OK);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }
}