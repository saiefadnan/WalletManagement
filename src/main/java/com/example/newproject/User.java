package com.example.newproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class User {
    public static double Balance,SUM;
    public static double[] A;
    public static double[] storeY;
    public static String Name;
    public static List<String> userInfo=new ArrayList<>();
    public static int goals_count,debts_count,lents_count;
    public static List<String> ap_name=new ArrayList<>();
    public static List<Double> ap_target=new ArrayList<>();
    public static List<Double> ap_saved=new ArrayList<>();
    public static List<String> ap_date=new ArrayList<>();
    public static List<String> ap_note=new ArrayList<>();
    public static List<String> ap_Dname=new ArrayList<>();
    public static List<Double> ap_debt=new ArrayList<>();
    public static List<Double> ap_repaid=new ArrayList<>();
    public static List<String> ap_Ddate=new ArrayList<>();
    public static List<String> ap_Dnote=new ArrayList<>();
    public static List<String> ap_Lname=new ArrayList<>();
    public static List<Double> ap_lent=new ArrayList<>();
    public static List<Double> ap_received=new ArrayList<>();
    public static List<String> ap_Ldate=new ArrayList<>();
    public static List<String> ap_Lnote=new ArrayList<>();
    public static List<FixedDeposit_data> FD_data;
    public static List<MonthlyBudget_data>MB_data;
    public static double net_worth;
    //something
    public static double[] Expense_Cat;
    public static int noti_num;
    public static int noti_counter;
    public static List<Notification_data>NF_data;
    public static Map<Date_CategoryKey, Double>Expense_data;
    public static ScheduledExecutorService scheduler;

    public User(String Name) throws SQLException, ClassNotFoundException {
        System.out.println("Account logged in for "+Name+"!!!");
        User.Name =Name;
        A=new double[11];
        Expense_data= new HashMap<>();
        storeY=new double[30];
        FD_data=new ArrayList<>();
        MB_data=new ArrayList<>();
        NF_data=new ArrayList<>();
        Expense_Cat=new double[11];
        int[] count=SQLConnection.getCount(Name);
        goals_count=count[0];
        debts_count=count[1];
        lents_count=count[2];
        SQLConnection.getInfo(Name);
        SQLConnection.wholeTable(Name);
        SQLConnection.wholeTableDebt(Name);
        SQLConnection.wholeTableLent(Name);
        SQLConnection.getDashboard(Name);
        SQLConnection.retrieveExpense(Name);
        SQLConnection.getMB(Name);
        SQLConnection.getFD(Name);
    }
    public static void reset()
    {
        SUM=0;
        Balance=0;
        A=new double[11];
        Expense_data=new HashMap<>();
        storeY=new double[30];
        FD_data=new ArrayList<>();
        MB_data=new ArrayList<>();
        ap_name=new ArrayList<>();
        ap_target=new ArrayList<>();
        ap_saved=new ArrayList<>();
        ap_date=new ArrayList<>();
        ap_note=new ArrayList<>();
        ap_Dname=new ArrayList<>();
        ap_debt=new ArrayList<>();
        ap_repaid=new ArrayList<>();
        ap_Ddate=new ArrayList<>();
        ap_Dnote=new ArrayList<>();
        ap_Lname=new ArrayList<>();
        ap_lent=new ArrayList<>();
        ap_received=new ArrayList<>();
        ap_Ldate=new ArrayList<>();
        ap_Lnote=new ArrayList<>();
        NF_data=new ArrayList<>();
        Expense_Cat=new double[11];
        noti_num=0;
        noti_counter=0;
        net_worth=0;
    }

}
