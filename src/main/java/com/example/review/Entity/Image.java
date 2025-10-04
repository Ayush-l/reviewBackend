package com.example.review.Entity;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Image {

    @Id
    private String id= UUID.randomUUID().toString();
    private String fileType;
    private byte[] file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }


    public byte[] getFile() {
        return file;
    }

}
