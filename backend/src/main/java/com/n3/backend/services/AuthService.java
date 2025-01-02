package com.n3.backend.services;

import com.n3.backend.utils.JwtUtil;
import com.n3.backend.utils.PasswordUtil;
import com.n3.backend.dto.*;
import com.n3.backend.dto.Auth.LoginRequest;
import com.n3.backend.dto.Auth.LoginResponse;
import com.n3.backend.dto.Auth.RegisterRequest;
import com.n3.backend.dto.User.User;
import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    public ApiResponse<LoginResponse> login(LoginRequest loginRequest){

        UserEntity user = userService.getByEmail(loginRequest.getEmail());
        if(user != null){
            if(PasswordUtil.validatePassword(loginRequest.getPassword(), user.getPassword())){
                JwtUtil jwtUtil = new JwtUtil();

                String token = jwtUtil.generateToken(user.getEmail());
                String expired = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtUtil.getClaimFromToken(token, Claims::getExpiration));

                return new ApiResponse<LoginResponse>(true, 200, new LoginResponse(new User(user), token, expired), "Login successfully");
            }
            else{
                return new ApiResponse<>(false, 300, null, "wrong password");
            }
        }
        else {
            return new ApiResponse<>(false, 400, null, "user does not exist");
        }
    }

    public ApiResponse<LoginResponse> register(RegisterRequest request){
        try {
            UserEntity user = userService.getByEmail(request.getEmail());
            if (user == null) {
                if (!PasswordUtil.isPasswordStrong(request.getPassword())) {
                    return new ApiResponse<>(false, 100, null, "valid password");
                } else {
                    UserEntity userNew = new UserEntity();
                    userNew.setAddress(request.getAddress());
                    userNew.setEmail(request.getEmail());
                    userNew.setFullname(request.getFullname());
                    userNew.setGender(request.getGender());
                    try {
                        userNew.setDateOfBirth(new Date((new SimpleDateFormat("yyyy/MM/dd")).parse(request.getDateOfBirth()).getTime()));
                    }
                    catch (Exception e){

                    }
//                    userNew.setBalance(0);
                    userNew.setPassword(PasswordUtil.encodePassword(request.getPassword()));

                    try {
                        userService.save(userNew);
                        JwtUtil jwtUtil = new JwtUtil();

                        String token = jwtUtil.generateToken(userNew.getEmail());
                        String expired = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtUtil.getClaimFromToken(token, Claims::getExpiration));
                        return new ApiResponse(true, 200, new LoginResponse(new User(userNew), token, expired), "register success");
                    }
                    catch (Exception e){
                        return new ApiResponse<>(false, 500, null, e.getMessage());
                    }
                }
            } else {
                return new ApiResponse<>(false, 400, null, "User has existed");
            }
        }
        catch(Exception e){
            return new ApiResponse<>(false, 500, null, e.getMessage());
        }
    }
}
