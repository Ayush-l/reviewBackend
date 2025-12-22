package com.example.review.Entity;

public class ImageInput {
    String url;
    String name;
    String jwtToken;

    public ImageInput(String jwtToken, String name, String url) {
        this.jwtToken = jwtToken;
        this.name = name;
        this.url = url;
    }

    public ImageInput() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
