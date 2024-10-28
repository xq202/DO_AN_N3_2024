package com.n3.backend;

import com.n3.backend.config.CustomAccessDeniedHandler;
import com.n3.backend.config.CustomAuthenticationPoint;
import com.n3.backend.config.JwtAuthFilter;
import com.n3.backend.repositories.UserRepository;
import com.n3.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationPoint customAuthenticationPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public ApiSecurityConfig(CustomAuthenticationPoint customAuthenticationPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationPoint = customAuthenticationPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
        return http.build();
    }
}
