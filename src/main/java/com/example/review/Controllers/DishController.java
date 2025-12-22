package com.example.review.Controllers;


import com.example.review.Entity.Body;
import com.example.review.Entity.Dish;
import com.example.review.Entity.ImageInput;
import com.example.review.FIlter.JwtAuthFilter;
import com.example.review.Services.DishServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth/dish")
public class DishController {
    private final DishServices dishServices;
    private final JwtAuthFilter jwtAuthFilter;
    private final String ROLE="admin";

    @Autowired
    DishController(DishServices dishServices,JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter=jwtAuthFilter;
        this.dishServices=dishServices;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createDish(@RequestBody ImageInput imageInput) {
        try{
            if(!jwtAuthFilter.doFilterInternal(imageInput.getJwtToken(),ROLE)) throw new Exception("forbidden");
            if(dishServices.createDish(imageInput.getName(), imageInput.getUrl())) return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            return new ResponseEntity<>(false,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false,HttpStatus.ALREADY_REPORTED);
        }
    }


    @PutMapping("/change/{dishName}/{id}")
    public ResponseEntity<Boolean> changeDish(@RequestBody Body body,@PathVariable String dishName,@PathVariable String id){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw new Exception("forbidden");
            if(dishServices.changeDish(dishName,id)) return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(false,HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteDish(@RequestBody ImageInput imageInput){
        try{
            if(!jwtAuthFilter.doFilterInternal(imageInput.getJwtToken(),ROLE)) return new ResponseEntity<>(false,HttpStatus.FORBIDDEN);
            dishServices.deleteDish(imageInput.getName());
            return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(false,HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/get")
    public ResponseEntity<ArrayList<Dish>> getDishes(){
        return new ResponseEntity<>(new ArrayList<>(dishServices.getAllDishes()),HttpStatus.OK);
    }


}
