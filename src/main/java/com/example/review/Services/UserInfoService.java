package com.example.review.Services;

import com.example.review.Entity.User;
import com.example.review.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserInfoService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserInfoDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt=userRepository.findById(username);
        if(userOpt.isEmpty()) throw new UsernameNotFoundException("NOT present in DB");
        User user=userOpt.get();
        return new UserInfoDetails(user);
    }
}
