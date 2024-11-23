package com.n3.backend.config;

import com.n3.backend.entities.UserEntity;
import com.n3.backend.services.UserService;
import com.n3.backend.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserService userService;
    public JwtAuthFilter(UserService userService) {
        this.userService = userService; // Gán giá trị từ Spring
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            JwtUtil jwtUtil = new JwtUtil();
            String token = authHeader.substring(7);
            UserEntity user = userService.getByEmail(jwtUtil.getSubjectByToken(token));
            if(user != null && jwtUtil.validateToken(token, user)){
//                System.out.println("token ok");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }
        else {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
