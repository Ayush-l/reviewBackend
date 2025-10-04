package com.example.review.Entity;


import org.springframework.stereotype.Component;

@Component
public class AuthRequest {

    String username;

    String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
