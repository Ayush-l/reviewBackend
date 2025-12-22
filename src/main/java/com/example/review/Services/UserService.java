package com.example.review.Services;


import com.example.review.Entity.Cafe;
import com.example.review.Entity.CafeToret;
import com.example.review.Entity.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.CafeToRetRepository;
import com.example.review.Repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserService {


    private static final Log log = LogFactory.getLog(UserService.class);
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final CafeService cafeService;
    private final CafeToRetRepository cafeToRetRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Autowired
    public UserService(ImageService imageService,CafeToRetRepository cafeToRetRepository,UserRepository userRepository, CafeRepository cafeRepository,CafeService cafeService){
        this.userRepository=userRepository;
        this.cafeRepository=cafeRepository;
        passwordEncoder=new BCryptPasswordEncoder();
        this.cafeService=cafeService;
        this.cafeToRetRepository=cafeToRetRepository;
        this.imageService=imageService;
    }

    public boolean createUser(String email, String password, String firstName, String lastName,String role){
        if(getUser(email)==null){
            User user=new User();
            user.setEmail(email);
            user.setRole(role.toLowerCase());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setReviews(new ArrayList<>());
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }


    public boolean changeUser(String email, String password, String firstName, String lastName){
        User user=getUser(email);
        if(user!=null){
            if(!passwordEncoder.matches(password,passwordEncoder.encode(password))) user.setPassword(passwordEncoder.encode(password));
            if(firstName!=null) user.setFirstName(firstName);
            if(lastName!=null) user.setLastName(lastName);
            userRepository.save(user);
            return true;
        }
        return false;
    }

//    @Transactional
//    public void changeDp(String email, MultipartFile file){
//        User user=getUser(email);
//        if(user.getImage()!=null) imageService.deleteImage(user.getImage().getId());
//        try{
//            String id=imageService.addFile(file);
//            user.setImage(imageService.loadFile(id));
//            userRepository.save(user);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }

//    @Transactional
//    public boolean deleteUser(String email,String passWord){
//        User user=getUser(email);
//        if(user!=null&&passwordEncoder.matches(user.getPassword(),passWord)){
//            for(Review review:user.getReviews()) reviewService.deleteReview(review.getId());
//            if(user.getCafeAdded()!=null) cafeService.deleteCafe(email,passWord);
//            userRepository.deleteById(email);
//            return true;
//        }
//        return false;
//    }


    public User getUser(String email){
        Optional<User> optUser=userRepository.findById(email);
        return optUser.orElse(null);
    }

    @Transactional
    public boolean addCafe(String email,String name,String address){
        User user=getUser(email);
        if(user!=null&&user.getRole().equals("owner")&&user.getCafeAdded()==null){
            Cafe cafe=new Cafe();
            cafe.setName(name);
            cafe.setAddress(address);
            cafe.setImages(new ArrayList<>());
            cafe.setDishes(new ArrayList<>());
            Cafe c=cafeRepository.save(cafe);
            user.setCafeAdded(c);
            userRepository.save(user);
            CafeToret cafeToret=new CafeToret();
            cafeToret.setName(name);
            cafeToret.setId(cafe.getId());
            cafeToret.setAddress(address);
            cafeToRetRepository.save(cafeToret);
            return true;
        }
        return false;
    }


}