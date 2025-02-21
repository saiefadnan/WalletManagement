package com.example.newproject;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class MonthlyBudget_data {
    public String Budget_name;
    public String selected_cat;
    public Color cat_color;
    public String period;
    public double limit_amount;
    public int Expense_index;
    public double expense_amount;
    public LocalDate init_date,final_date;
    public boolean notify1,notify2;
    //------------------>
    public double[] Ypoints;
    public double progress;
    public LocalDate Today1;
    public LocalDate Today2;
    public LocalDateTime init_time;
    public ScheduledExecutorService periodic_update;
    public ScheduledFuture<?> scheduledTask1;
    public ScheduledExecutorService Notification_checker;
    public ScheduledFuture<?> scheduledTask2;
    public ScheduledExecutorService Chart_updater;
    public ScheduledFuture<?> scheduledTask3;

    MonthlyBudget_data(){
        Ypoints=new double[30];
    }
    public MonthlyBudget_data(String budgetName, String cat, int index, Color color, String period, double limit, double expense, String idate, String fdate, boolean not1, boolean not2) {
        Budget_name = budgetName;
        selected_cat = cat;
        cat_color = color;
        this.period = period;
        limit_amount = limit;
        progress = 0;
        Expense_index = index;
        expense_amount = expense;
        Ypoints=new double[30];
        Today1=LocalDate.now();
        Today2=LocalDate.now();
        init_date=LocalDate.parse(idate);
        final_date=LocalDate.parse(fdate);
        init_time=LocalDateTime.now();
        notify1=not1;
        notify2=not2;
    }
}
