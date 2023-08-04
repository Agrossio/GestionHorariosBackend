package com.asj.gestionhorarios.config.websocket;

import com.asj.gestionhorarios.security.exception.handler.WebSocketHandlerCustom;
import com.asj.gestionhorarios.security.filter.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandlerCustom handlerCustom;
    private final WebSocketHandshakeInterceptor wsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handlerCustom, "/api/v1/logged")
                .setAllowedOrigins("*")
                .addInterceptors(wsInterceptor);
    }
}