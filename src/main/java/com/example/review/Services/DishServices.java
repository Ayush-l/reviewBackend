package com.example.review.Services;


import com.example.review.Entity.Dish;
import com.example.review.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishServices{
    @Autowired
    private final DishRepository dishRepository;

    public DishServices(DishRepository dishRepository){
        this.dishRepository=dishRepository;
    }

    public boolean createDish(String name,String url){
        Dish d=getDish(name);
        if(d==null){
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
        if(d!=null){
            d.setUrl(url);
            dishRepository.save(d);
            return true;
        }
        return false;
    }

    public Dish getDish(String name){
        Optional<Dish> optDish=dishRepository.findById(name.toLowerCase());
        return optDish.orElse(null);
    }

    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }
}
