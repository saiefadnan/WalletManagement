package com.example.newproject;

import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification_data {
    public Text messsage;
    public LocalDate localdate;
    public LocalTime localtime;
    public Notification_data(){
        messsage=new Text();
    }

    public Notification_data(String title, String message, String date) {
        messsage=new Text();
        messsage.setText(title+"\n"+message+"\n"+date);
    }
}
