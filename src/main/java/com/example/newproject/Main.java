package com.example.newproject;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new Thread(()->{
            Supabase.getInstance();
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Supabase.getInstance().closeConnection();
        }));

        HelloApplication.main(args);
    }
}
