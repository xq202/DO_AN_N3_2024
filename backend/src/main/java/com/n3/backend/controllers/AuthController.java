package com.n3.backend.controllers;

import com.n3.backend.dto.*;
import com.n3.backend.dto.Auth.LoginRequest;
import com.n3.backend.dto.Auth.LoginResponse;
import com.n3.backend.dto.Auth.RegisterRequest;
import com.n3.backend.dto.User.User;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.services.AuthService;
import com.n3.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<User> getUser(){
        UserEntity user = userService.getCurrentUser();
        if (user == null) return new ApiResponse(false, 400, null, "User not found");
        return new ApiResponse(true, 200, new User(user), "success");
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
