package com.example.newproject;

import org.glassfish.tyrus.server.Server;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/websocket")
public class WebSocketServer {
    public static Server server;
    public static void startServer(CountDownLatch serverReadyLatch) {
        server = new Server("localhost", 8080, "/", null, WebSocketServer.class);
        try {
            server.start();
            System.out.println("WebSocket server running on ws://localhost:8080/websocket");
            serverReadyLatch.countDown();  // Notify that server is ready
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopServer() {
        if (server != null) {
            server.stop();
            System.out.println("WebSocket server stopped.");
        }
    }
    @OnOpen
    public void onOpen(Session session){
        System.out.println("server: Client connected: "+ session.getId());
    }
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("server: message received: "+message);
        session.getBasicRemote().sendText("echo: "+ message);
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("server: Client disconnected: " + session.getId());
    }
}
