package com.n3.backend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encodePassword(String password){
        return encoder.encode(password);
    }

    public static boolean validatePassword(String rawPassword, String encodedPassword){
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static boolean isPasswordStrong(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
}
