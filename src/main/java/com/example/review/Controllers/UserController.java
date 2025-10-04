package com.example.review.Controllers;


import com.example.review.Entity.Image;
import com.example.review.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController{
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }


    @PutMapping("/change")
    public ResponseEntity<Boolean> changeUser(@RequestBody String password, @RequestBody String firstName, @RequestBody String lastName){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.changeUser(email,password,firstName,lastName)) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/change/dp")
    public ResponseEntity<Void> changeDp(@RequestParam("file") MultipartFile file){
        userService.changeDp(SecurityContextHolder.getContext().getAuthentication().getName(),file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestBody String password){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.deleteUser(email,password)) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

}
