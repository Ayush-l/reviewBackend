package com.example.review.Controllers;


import com.example.review.Entity.Body;
import com.example.review.Entity.User;
import com.example.review.FIlter.JwtAuthFilter;
import com.example.review.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController{
    private final UserService userService;
    private final JwtAuthFilter jwtAuthFilter;
    private final String ROLE="user";

    @Autowired
    public UserController(UserService userService,JwtAuthFilter jwtAuthFilter){
        this.userService=userService;
        this.jwtAuthFilter=jwtAuthFilter;
    }


    @PutMapping("/change")
    public ResponseEntity<Boolean> changeUser(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw  new Exception("FORBIDDEN");
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            if(userService.changeUser(email,body.getUser().getPassword(),body.getUser().getFirstName(),body.getUser().getLastName())) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
            return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

//    @PutMapping("/change/dp")
//    public ResponseEntity<Void> changeDp(@RequestBody Body body){
//        try{
//            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw  new Exception("FORBIDDEN");
//            userService.changeDp(SecurityContextHolder.getContext().getAuthentication().getName(),body.getFile());
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Boolean> deleteUser(@RequestBody Body body){
//        try{
//            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw  new Exception("FORBIDDEN");
//            if(userService.deleteUser(body.getUser().getEmail(),body.getUser().getPassword())) return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
//            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//    }


    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestBody Body body){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(),ROLE)) throw  new Exception("FORBIDDEN");
            return new ResponseEntity<>(userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName()),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
