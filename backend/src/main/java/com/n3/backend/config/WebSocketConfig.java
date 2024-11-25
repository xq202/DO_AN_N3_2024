package com.n3.backend.config;

import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.services.InfoPackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private InfoPackingService infoPackingService;
    @Bean
    protected InfoWebSocketHandler webSocketHandler(InfoPackingService infoPackingService) {
        return new InfoWebSocketHandler(infoPackingService);
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(infoPackingService), "/ws/info").setAllowedOrigins("*").setAllowedOriginPatterns("*");
    }
}
