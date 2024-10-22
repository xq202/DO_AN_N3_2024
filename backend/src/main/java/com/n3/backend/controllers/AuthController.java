package com.n3.backend.controllers;

import com.n3.backend.dto.*;
import com.n3.backend.services.AuthService;
import com.n3.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ApiResponse<LoginResponse> login(@ModelAttribute LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping(value = "/register")
    public ApiResponse<LoginResponse> register(@ModelAttribute RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/user")
    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            return new User(userService.getByEmail(((UserDetails) authentication.getPrincipal()).getUsername()));
        }
        else return null;
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
