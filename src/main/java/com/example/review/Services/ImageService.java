package com.example.review.Services;


import com.example.review.Entity.Cafe;
import com.example.review.Entity.Image;
import com.example.review.Entity.User;
import com.example.review.Repositories.CafeRepository;
import com.example.review.Repositories.ImageRepository;
import com.example.review.Repositories.UserRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {


    private static final Log log = LogFactory.getLog(ImageService.class);
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CafeRepository cafeRepository;

    public boolean deleteImage(String id){
        imageRepository.deleteById(id);
        return true;
    }

    @Transactional
    public String addDP(MultipartFile file,String id){
        try {
            Image image = new Image();
            image.setFileType(file.getContentType());
            image.setFile(IOUtils.toByteArray(file.getInputStream()));
            User user = userRepository.findById(id).get();
            deleteImage(user.getImage().getId());
            image.setUser(user);
            String s=imageRepository.save(image).getId();
            user.setImage(imageRepository.findById(s).get());
            userRepository.save(user);
            return s;
        } catch (Exception e) {
            log.info(e);
            return "Not Created";
        }
    }

    @Transactional
    public boolean addCafeImage(List<String> url, String id){
        try {
            Cafe cafe=cafeRepository.findById(id).get();
            cafe.setImages(url);
            cafeRepository.save(cafe);
            return true;
        } catch (Exception e) {
            log.info(e);
            return false;
        }
    }



    public String addFile(MultipartFile toUpload) throws IOException {
        Image image=new Image();
        image.setFileType(toUpload.getContentType());
        image.setFile(IOUtils.toByteArray(toUpload.getInputStream()));
        return imageRepository.save(image).getId();
    }

    public Image loadFile(String id) throws IOException {
        return imageRepository.findById(id).orElse(null);
    }

}
