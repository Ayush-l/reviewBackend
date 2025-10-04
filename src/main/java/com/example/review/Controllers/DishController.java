package com.example.review.Controllers;


import com.example.review.Entity.Dish;
import com.example.review.Services.DishServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth/dish")
@RestController
public class DishController {
    private final DishServices dishServices;

    @Autowired
    DishController(DishServices dishServices){
        this.dishServices=dishServices;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createDish(@RequestBody Dish dish){
        if(dishServices.createDish(dish.getName(),dish.getImage())) return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/change")
    public ResponseEntity<Boolean> changeDish(@RequestBody Dish dish){
        if(dishServices.changeDish(dish.getName(),dish.getImage())) return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }
}
