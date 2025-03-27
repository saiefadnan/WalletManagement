package com.example.newproject;

import javafx.scene.text.Text;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Notification_data {
    public String message;
    public LocalDate localdate;
    public LocalTime localtime;
    public Notification_data(String msg){
       this.message = msg;
       this.localdate = LocalDate.now();
       this.localtime = LocalTime.now();
    }

    public void showSystemNotification() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/wallet-logo.jpg"));

            TrayIcon trayIcon = new TrayIcon(image, "App Notification");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Notification");

            try {
                tray.add(trayIcon);
                trayIcon.displayMessage("Notification", message, TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("System tray not supported");
        }
    }

}
