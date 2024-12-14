package com.n3.backend.config;

import com.n3.backend.handler.CustomAccessDeniedHandler;
import com.n3.backend.handler.CustomAuthenticationPoint;
import com.n3.backend.repositories.UserRepository;
import com.n3.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
        http.csrf().disable()
                .cors().configurationSource(corsFilter())
                .and()
//                .requiresChannel(
//                        channel -> channel.anyRequest().requiresSecure())
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("guest/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/vnpay/**").permitAll()
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

    @Bean
    public CorsConfigurationSource corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Cho phép origin cụ thể
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // Áp dụng cho tất cả endpoint
        return source;
    }
}
