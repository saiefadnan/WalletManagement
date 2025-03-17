package com.example.newproject;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class InternetConnection {
    private static Session session;
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("client: Connection established: "+ session.getId());

        sendMessage("hello server!!! I'm client!!!");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("client: Message received: " + message);
    }

    public void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            } else {
                System.out.println("Session is closed. Cannot send message.");
            }
        } catch (IOException e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("client: disconnected: " + session.getId() + " due to " + closeReason.getReasonPhrase());
        System.out.println("Close code: " + closeReason.getCloseCode());
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("Error: " + throwable.getMessage());
    }
}
