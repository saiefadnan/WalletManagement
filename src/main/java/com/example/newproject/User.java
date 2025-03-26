package com.example.newproject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public class User {
    public static int id;
    public static double Balance,SUM;
    public static double[] A;
    public static double[] storeY;
    public static String Name;
    public static List<String> userInfo=new ArrayList<>();
    public static int goals_count,debts_count,lents_count;
    public static List<String> ap_name=new ArrayList<>();
    public static List<Double> ap_target=new ArrayList<>();
    public static List<Double> ap_saved=new ArrayList<>();
    public static List<LocalDate> ap_date=new ArrayList<>();
    public static List<String> ap_note=new ArrayList<>();
    public static List<String> ap_Dname=new ArrayList<>();
    public static List<Double> ap_debt=new ArrayList<>();
    public static List<Double> ap_repaid=new ArrayList<>();
    public static List<LocalDate> ap_Ddate=new ArrayList<>();
    public static List<String> ap_Dnote=new ArrayList<>();
    public static List<String> ap_Lname=new ArrayList<>();
    public static List<Double> ap_lent=new ArrayList<>();
    public static List<Double> ap_received=new ArrayList<>();
    public static List<LocalDate> ap_Ldate=new ArrayList<>();
    public static List<String> ap_Lnote=new ArrayList<>();
    public static List<FixedDeposit> FD_data;
    public static List<BudgetPlan>MB_data;
    public static double net_worth;
    //something
    public static double[] Expense_Cat;
    public static int noti_num;
    public static int temp_unread_notif;
    public static int noti_counter;
    public static List<Notification_data>NF_data;
    public static Map<Date_CategoryKey, Double>Expense_data;
    public static Map<String, Double> BalanceGraph;
    public static ScheduledExecutorService scheduler;

    public User(String Name) throws SQLException, ClassNotFoundException {
        A=new double[11];
        Expense_data= new HashMap<>();
        BalanceGraph = new HashMap<>();
        storeY=new double[30];
        Arrays.fill(storeY,-1);
        FD_data=new ArrayList<>();
        MB_data=new ArrayList<>();
        NF_data=new ArrayList<>();
        Expense_Cat=new double[11];
        goals_count=0;
        debts_count=0;
        lents_count=0;
        temp_unread_notif=0;
        noti_num=0;
        noti_counter=0;
        net_worth=0;
        fetch_User_data();
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
        temp_unread_notif=0;
        noti_num=0;
        noti_counter=0;
        net_worth=0;
        LoginManager.clearLoginInfo();
    }

    private void fetch_User_data(){
        Supabase.getInstance().getUserInfo();
        Supabase.getInstance().fetchNotifs();
        Supabase.getInstance().getFinancialRecords();
        Supabase.getInstance().getBudgetInfo();
        Supabase.getInstance().getFixedDepositInfo();
        Supabase.getInstance().getExpenseByDate();
        Supabase.getInstance().getBalanceBydate();
        Supabase.getInstance().getLentsInfo();
        Supabase.getInstance().getDebtsInfo();
        Supabase.getInstance().getGoalsInfo();
//        SQLConnection.wholeTable(Name);
//        SQLConnection.wholeTableDebt(Name);
//        SQLConnection.wholeTableLent(Name);
        //SQLConnection.retrieveExpense(Name);
    }
}
