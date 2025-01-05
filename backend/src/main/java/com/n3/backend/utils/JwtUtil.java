package com.n3.backend.utils;

import com.n3.backend.entities.UserEntity;
import com.n3.backend.repositories.UserRepository;
import com.n3.backend.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {
    private String secretKey = Base64.getEncoder().encodeToString("DO_AN_N3_2024_DO_AN_N3_2024_DO_AN_N3_2024".getBytes());
    private long expirationMs = 3600000;
    private UserRepository userRepository;

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token) // Sửa 'parseClaimsJwt' thành 'parseClaimsJws'
                    .getBody();
            return claimResolver.apply(claims);
        } catch (JwtException e) {
            // Xử lý lỗi phân tích token
            System.out.println("Token is invalid: " + e.getMessage());
            return null;
        } catch (Exception e) {
            // Xử lý lỗi khác
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }

    public String generateToken(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public static String generateTokenForver(String subject){
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString("DO_AN_N3_2024_DO_AN_N3_2024_DO_AN_N3_2024".getBytes()))
                .compact();
    }

    public String getSubjectByToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date expiration = getClaimFromToken(token, Claims::getExpiration);

        if(expiration == null){
            return false;
        }
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserEntity user){
        System.out.println(getSubjectByToken(token));
        return (getSubjectByToken(token).equals(user.getEmail()) && (!isTokenExpired(token)));
    }
}
