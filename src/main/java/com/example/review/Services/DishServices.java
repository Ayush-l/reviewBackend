package com.example.review.Services;


import com.example.review.Classes.Dish;
import com.example.review.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class DishServices{
    @Autowired
    private DishRepository dishRepository;

    public boolean createDish(String name,String url){
        Dish d=getDish(name);
        if(d.equals(null)){
            d=new Dish();
            d.setName(name.toLowerCase());
            d.setUrl(url);
            dishRepository.save(d);
            return true;
        }
        return false;
    }

    public boolean changeDish(String name,String url){
        Dish d=getDish(name);
        if(!d.equals(null)){
            d.setUrl(url);
            dishRepository.save(d);
            return true;
        }
        return false;
    }

    public Dish getDish(String name){
        return dishRepository.findById(name.toLowerCase()).get();
    }

    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }
}
