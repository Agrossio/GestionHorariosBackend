package com.asj.gestionhorarios.security.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketHandlerCustom implements WebSocketHandler {
    private static List<String> contadorSesiones = new ArrayList<>();
    private static List<WebSocketSession> sesionesAbiertas = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocketHandlerCustom.afterConnectionEstablished");
        sesionesAbiertas.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("WebSocketHandlerCustom.handleMessage");
        String email = message.getPayload().toString();
        if(!contadorSesiones.contains(email)) {
            contadorSesiones.add(email);
            session.getAttributes().put("email", email);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String contadorSesionesJson = objectMapper.writeValueAsString(contadorSesiones);
        for (WebSocketSession sesion : sesionesAbiertas) {
            sesion.sendMessage(new TextMessage(contadorSesionesJson));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sesionesAbiertas.remove(session);
        String email = getEmailFromSession(session);
        contadorSesiones.remove(email);
        ObjectMapper objectMapper = new ObjectMapper();
        String contadorSesionesJson = objectMapper.writeValueAsString(contadorSesiones);
        for (WebSocketSession sesion : sesionesAbiertas) {
            sesion.sendMessage(new TextMessage(contadorSesionesJson));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public List<String> obtenerNumeroSesionesConectadas() {
        return contadorSesiones;
    }

    private String getEmailFromSession(WebSocketSession session) {
        String email = (String) session.getAttributes().get("email");
        return email != null ? email : "";
    }
}