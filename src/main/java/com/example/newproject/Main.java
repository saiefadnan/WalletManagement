package com.example.newproject;

import java.sql.SQLException;

public class Main {
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
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        connect_Database_On_New_thread();
        close_Database_Connection();
        HelloApplication.main(args);
    }
}
