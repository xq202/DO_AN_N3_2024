package com.n3.backend.controllers;

import com.n3.backend.dto.LoginRequest;
import com.n3.backend.dto.LoginResponse;
import com.n3.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public LoginResponse login(@ModelAttribute LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
