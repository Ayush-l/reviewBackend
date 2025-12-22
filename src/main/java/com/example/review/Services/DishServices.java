package com.example.review.Services;


import com.example.review.Entity.Dish;
import com.example.review.Repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishServices{

    private final DishRepository dishRepository;
    private final ImageService imageService;
    @Autowired
    public DishServices(DishRepository dishRepository,ImageService imageService){
        this.dishRepository=dishRepository;
        this.imageService=imageService;
    }

    public boolean createDish(String name,String url){
        Dish d=getDish(name.toLowerCase());
        if(d==null){
            d=new Dish();
            d.setName(name.toLowerCase());
            d.setUrl(url);
            dishRepository.save(d);
            return true;
        }
        return false;
    }

    public boolean changeDish(String name, String url){

        Dish d=getDish(name);
        try{
            if(d!=null) {
                d.setUrl(url);
                dishRepository.save(d);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Dish getDish(String name){
        Optional<Dish> optDish=dishRepository.findById(name);
        return optDish.orElse(null);
    }

    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }

    public boolean deleteDish(String name){
        dishRepository.deleteById(name);
        return true;
    }
}
