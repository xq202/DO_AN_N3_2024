package com.n3.backend.config;

import com.n3.backend.handler.InfoWebSocketHandler;
import com.n3.backend.services.PackingInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private PackingInformationService packingInformationService;
    @Bean
    protected InfoWebSocketHandler webSocketHandler(PackingInformationService packingInformationService) {
        return new InfoWebSocketHandler(packingInformationService);
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(packingInformationService), "/ws/info").setAllowedOrigins("*").setAllowedOriginPatterns("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout((long)(1000 * 60));
        return container;
    }
}
