package com.example.review.Controllers;


import com.example.review.Entity.Body;
import com.example.review.Entity.Image;
import com.example.review.FIlter.JwtAuthFilter;
import com.example.review.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class ImageController {


    private final JwtAuthFilter jwtAuthFilter;
    private final ImageService imageService;
    private final String ANY="any";
    private final String USER="user";

    @Autowired
    ImageController(JwtAuthFilter jwtAuthFilter,ImageService imageService){
        this.imageService=imageService;
        this.jwtAuthFilter=jwtAuthFilter;
    }

    @PostMapping("/uploaddp/{id}")
    public ResponseEntity<String> uploadDp(@RequestBody Body body, @PathVariable String id) throws IOException {
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(), ANY)){
                throw new Exception("FORBIDDEN");
            }
            return new ResponseEntity<>(imageService.addDP(body.getFile(),id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PostMapping("/uploadcafeimage")
    public ResponseEntity<Boolean> uploadCafeImage(@RequestBody Body body) throws IOException {
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(), "owner")){
                throw new Exception("FORBIDDEN");
            }
            if(imageService.addCafeImage(body.getCafe().getImages(), body.getCafe().getId())) return new ResponseEntity<>(true,HttpStatus.OK);
            return new ResponseEntity<>(false,HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@RequestBody Body body,@PathVariable String id) throws IOException {


        try{

            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(), ANY)){
                throw new Exception("FORBIDDEN");
            }
            Image loadFile = imageService.loadFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(loadFile.getFileType() ))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getId() + "\"")
                    .body(new ByteArrayResource(loadFile.getFile()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteDish(@RequestBody Body body,@PathVariable String id){
        try{
            if(!jwtAuthFilter.doFilterInternal(body.getAuthToken(), ANY)){
                throw new Exception("FORBIDDEN");
            }
            return new ResponseEntity<>(imageService.deleteImage(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}