package com.n3.backend.controllers;

import com.n3.backend.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/test")
    public String test(){
        return JwtUtil.generateTokenForver("admin@gmail.com");
    }
}
