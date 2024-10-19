package com.n3.backend.services;

import com.n3.backend.config.JwtUtil;
import com.n3.backend.dto.LoginRequest;
import com.n3.backend.dto.LoginResponse;
import com.n3.backend.dto.User;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class AuthService {
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public LoginResponse login(LoginRequest loginRequest){

        UserEntity user = userService.getByEmail(loginRequest.getEmail());
        if(user != null){
            if(user.getPassword().trim().equals(loginRequest.password.trim())){
                JwtUtil jwtUtil = new JwtUtil();

                String token = jwtUtil.generateToken(user.getEmail());
                String expired = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtUtil.getClaimFromToken(token, Claims::getExpiration));

                return new LoginResponse(true, new User(user), token, expired, "success");
            }
            else{
                return new LoginResponse(false, null, null, null, "password fail");
            }
        }
        else {
            return new LoginResponse(false, null, null, null, "user error");
        }
    }
}
