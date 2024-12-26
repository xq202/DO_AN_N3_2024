package com.n3.backend.handler;

import com.google.gson.Gson;
import com.n3.backend.dto.SlotInfo;
import com.n3.backend.services.PackingInformationService;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InfoWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final PackingInformationService packingInformationService;
    public InfoWebSocketHandler(PackingInformationService packingInformationService) {
        this.packingInformationService = packingInformationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(message.getPayload().equals("getInfo")) {
            SlotInfo slotInfo = packingInformationService.getSlotInfo();

            Gson gson = new Gson();
            String json = gson.toJson(slotInfo);

            session.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcastMessage(Object message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendSlotInfo(){
        SlotInfo slotInfo = packingInformationService.getSlotInfo();
        broadcastMessage(slotInfo);
    }
}
