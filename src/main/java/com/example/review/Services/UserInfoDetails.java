package com.example.review.Services;

import com.example.review.Entity.User;



public class UserInfoDetails {

    private String username;
    private String password;
    private String authority;

    public UserInfoDetails(User user){
        username=user.getEmail();
        password=user.getPassword();
        this.authority=user.getRole();
    }

    public String getAuthority() {
        return authority;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
