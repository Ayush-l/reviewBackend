package com.example.review.FIlter;

import com.example.review.Services.JWTService;
import com.example.review.Services.UserInfoDetails;
import com.example.review.Services.UserInfoService;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthFilter{
    private final JWTService jwtService;
    private final UserInfoService userInfoService;

    @Autowired
    public JwtAuthFilter(JWTService jwtService, UserInfoService userInfoService){
        this.jwtService=jwtService;
        this.userInfoService=userInfoService;
    }





    public boolean doFilterInternal(String authHeader,String role) throws ServletException, IOException {
        String token=null;
        String username=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtService.extractUsername(token);
        }
        else return false;
        if(username!=null){
            UserInfoDetails user=userInfoService.loadUserByUsername(username);
            if(role.equals("any") || jwtService.validateToken(token,user)&&user.getAuthority().equals(role)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(user.getUsername(),null);
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return true;
            }
        }
        return false;
    }
}
