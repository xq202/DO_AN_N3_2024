package com.n3.backend.dto.Auth;

import com.n3.backend.dto.User.User;

public class LoginResponse {
    public User user;
    public String token;
    public String expiredAt;

    public LoginResponse(User user, String token, String expiredAt) {
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
