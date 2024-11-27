package com.n3.backend.controllers;

import com.n3.backend.dto.ApiResponse;
import com.n3.backend.dto.DtoPage;
import com.n3.backend.dto.User.User;
import com.n3.backend.dto.User.UserRequest;
import com.n3.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DtoPage<User>> getListUser(@ModelAttribute UserRequest request){
        return userService.getListUser(request);
    }
}
