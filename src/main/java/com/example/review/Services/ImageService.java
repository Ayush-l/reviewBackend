package com.example.review.Services;


import com.example.review.Entity.Image;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public void deleteImage(String id){
        template.delete(new Query(Criteria.where("_id").is(id)));
    }

    public String addFile(MultipartFile toUpload) throws IOException {
        DBObject metaData=new BasicDBObject();
        Object fileId=template.store(toUpload.getInputStream(),toUpload.getOriginalFilename(),toUpload.getContentType(),metaData);
        return fileId.toString();
    }

    public Image loadFile(String id) throws IOException {
        GridFSFile gridFSFile=template.findOne(new Query(Criteria.where("_id").is(id)));
        Image image=new Image();
        if(gridFSFile!=null && gridFSFile.getMetadata() != null){
            image.setId(gridFSFile.getId().toString());
            image.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            image.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }
        return image;
    }

}
