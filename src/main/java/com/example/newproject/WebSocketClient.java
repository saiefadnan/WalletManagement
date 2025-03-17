package com.example.newproject;
import javax.websocket.*;
import java.net.URI;

public class WebSocketClient {
    private final static String server_uri="ws://localhost:8080/websocket";
    public static void getClientConnected(){
        try{
            WebSocketContainer cont = ContainerProvider.getWebSocketContainer();
            cont.connectToServer(InternetConnection.class, new URI(server_uri));
            }
        catch (Exception e){
            System.out.println("Client Connection failed: " + e.getMessage());
        }

    }
}
