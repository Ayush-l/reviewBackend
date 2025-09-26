package com.example.review.Services;


import com.example.review.Classes.Cafe;
import com.example.review.Classes.Dish;
import com.example.review.Classes.Review;
import com.example.review.Classes.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.DishRepository;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CafeService {

    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishRepository dishRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Cafe getCafe(Long id){
        return cafeRepository.findById(id).get();
    }

    public boolean createCafe(Long id){
        if(getCafe(id).equals(null)){
            Cafe cafe=new Cafe();
            cafe.setId(id);
            cafe.setDishesCount(0);
            cafe.setCafeRating(0);
            cafe.setCafeRating(0);
            cafeRepository.save(cafe);
            return true;
        }
        return false;
    }

    public boolean addReview(Long id, Review review){
        Cafe cafe=getCafe(id);
        if(cafe.equals(null)) return false;
        cafe.addReview(review);
        cafeRepository.save(cafe);
        return true;
    }

    public boolean addDish(String name,String url,Long id){
        Cafe cafe=getCafe(id);
        if(cafe.equals(null)) return false;
        Dish dish=dishRepository.findById(name).get();
        if(!dish.equals(null)){
            cafe.addDish(dish);
            cafeRepository.save(cafe);
        }
        return true;
    }

    public boolean deleteCafe(Long id,String passWord){
        Cafe cafe=getCafe(id);
        User user=userRepository.findById(cafe.getUser().getEmail()).get();
        if(!user.equals(null)&&passwordEncoder.matches(user.getPassword(),passWord)){
            cafeRepository.deleteById(id);
            user.setCafeAdded(null);
            userRepository.save(user);
        }
        return false;
    }
}
