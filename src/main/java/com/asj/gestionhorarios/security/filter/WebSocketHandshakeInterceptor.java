package com.asj.gestionhorarios.security.filter;

import com.asj.gestionhorarios.security.service.JWTTokenService;
import com.asj.gestionhorarios.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JWTTokenService jwtTokenService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            URI uri = request.getURI();
            MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
            String token = queryParams.getFirst("token");
            String username = jwtTokenService.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            if (jwtTokenService.validateToken(token, userDetails)) {
                return true;
            }
            return false;
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("WebSocketHandshakeInterceptor.afterHandshake" + request.getHeaders().toString());
        if(request.getHeaders() == null )
            return ;
        if(request.getHeaders().get("Sec-WebSocket-Protocol") == null)
            return ;
        String protocol = (String) request.getHeaders().get("Sec-WebSocket-Protocol").get(0);
        if(protocol == null)
            return ;

        response.getHeaders().add("Sec-WebSocket-Protocol", protocol);
    }
}
