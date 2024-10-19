package com.n3.backend.dto;

public class LoginResponse {
    public boolean status;
    public User user;
    public String token;
    public String expiredAt;
    public String message;

    public LoginResponse(boolean status, User user, String token, String expiredAt, String message) {
        this.status = status;
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
        this.message = message;
    }
}
