package com.example.newproject;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static CountDownLatch serverReadyLatch = new CountDownLatch(1);
    private static Thread thread;
    public static void connect_Database_On_New_thread(){
        try {
            if (thread != null && thread.isAlive()) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = new Thread(()->{
            System.out.println("Attempting to connect...");
            Supabase.getInstance();
        });
        thread.start();
    }
    public static void close_Database_Connection(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Supabase.getInstance().closeConnection();
            System.out.println("Connection closed.");
        }));
    }
    public static void closeWebSocketServer(){
        Runtime.getRuntime().addShutdownHook(new Thread(WebSocketServer::stopServer));
        //System.out.println("Web socket server closed.");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Thread(() -> {
            WebSocketServer.startServer(serverReadyLatch);
        }).start();
        new Thread(() -> {
            try {
                serverReadyLatch.await(); // Wait until the server is ready
                WebSocketClient.getClientConnected();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        connect_Database_On_New_thread();
        close_Database_Connection();
        closeWebSocketServer();
        HelloApplication.main(args);
    }
}
