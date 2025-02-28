package com.example.newproject;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.paint.Color;

import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Supabase {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");
    private static Supabase instance;
    private Connection conn;

    public Supabase() {
        try {
            conn =  DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Supabase PostgreSQL!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Supabase getInstance(){
        if(instance==null) instance= new Supabase();
        return instance;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ALL QUERIES

    public void fetchData(){
        try{
            if (conn != null) {
                System.out.println("query ongoing...");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM test");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Name: " + rs.getString("name"));
                }

            } else {
                System.out.println("Connection failed.");
                Main.connect_Database_On_New_thread();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean signupController(String[] signup){
        try{
            if(conn!=null){
                System.out.println("signup request ongoing...");
                PreparedStatement ps = conn.prepareStatement("INSERT INTO userinfo (firstname,lastname,name,email,password,gender,dob,profession,address,phone) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?)");
                ps.setString(1,signup[0]);
                ps.setString(2,signup[1]);
                ps.setString(3,signup[2]);
                ps.setString(4,signup[3]);
                ps.setString(5,signup[4]);
                ps.setString(6,signup[5]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate dob =LocalDate.parse(signup[6], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ps.setDate(7, java.sql.Date.valueOf(dob));
                ps.setString(8,signup[7]);
                ps.setString(9,signup[8]);
                ps.setString(10,signup[9]);
                int rs = ps.executeUpdate();
                if(rs>0){
                    System.out.println("sign up successful...");
                    return true;
                }
                else{
                    System.out.println("failure occured...try again");
                    return false;
                }
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
                return false;
            }

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginController(String name, String password){
        try{
            if(conn!=null){
                System.out.println("checking user credentials...");
                PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM userinfo WHERE name=? and password=?");
                ps.setString(1,name);
                ps.setString(2,password);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("Try again...");
                    return false;
                }

                User.id = rs.getInt("id");
                User.Name = rs.getString("name");
                System.out.println("login successful, userId: "+User.id+ "  Name: "+User.Name);

                return true;
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
                return false;
            }

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void getUserInfo(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT firstname,lastname,email,gender,dob,profession,address,phone FROM userinfo WHERE id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                }
                    User.userInfo.add(rs.getString("firstname"));
                    User.userInfo.add(rs.getString("lastname"));
                    User.userInfo.add(rs.getString("email"));
                    User.userInfo.add(rs.getString("gender"));
                    User.userInfo.add(rs.getString("dob"));
                    User.userInfo.add(rs.getString("profession"));
                    User.userInfo.add(rs.getString("address"));
                    User.userInfo.add(rs.getString("phone"));

                    System.out.println("UserInfo retrieved...!" );
                    for(int i=0;i<User.userInfo.size();++i){
                        System.out.println(User.userInfo.get(i));
                    }
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateUserInfo(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE userinfo set " +
                        "firstname=?,lastname=?,email=?,gender=?,dob=?,profession=?,address=?,phone=? WHERE id=?");
                ps.setString(1, User.userInfo.get(0));
                ps.setString(2, User.userInfo.get(1));
                ps.setString(3, User.userInfo.get(2));
                ps.setString(4, User.userInfo.get(3));
                LocalDate ld =LocalDate.parse(User.userInfo.get(4), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                ps.setDate(5, java.sql.Date.valueOf(ld));
                ps.setString(6, User.userInfo.get(5));
                ps.setString(7, User.userInfo.get(6));
                ps.setString(8, User.userInfo.get(7));
                ps.setInt(9, User.id);
                int ru = ps.executeUpdate();

                if(ru>0){
                    System.out.println("UserInfo updated..." );
                }
                else System.out.println("Error occured...Try again" );
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void getFinancialRecords(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM financial_records WHERE id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }
                User.Balance=rs.getDouble("balance");
                User.Expense_Cat[0]=rs.getDouble("food");
                User.Expense_Cat[1]=rs.getDouble("shopping");
                User.Expense_Cat[2]=rs.getDouble("housing");
                User.Expense_Cat[3]=rs.getDouble("transportation");
                User.Expense_Cat[4]=rs.getDouble("vehicle");
                User.Expense_Cat[5]=rs.getDouble("entertainment");
                User.Expense_Cat[6]=rs.getDouble("investments");
                User.Expense_Cat[7]=rs.getDouble("incomes");
                User.Expense_Cat[8]=rs.getDouble("communication");
                User.Expense_Cat[9]=rs.getDouble("fin_expenses");
                User.Expense_Cat[10]=rs.getDouble("others");

                System.out.println("Financial records retrieved...!" );
                for(int i=0;i<User.userInfo.size();++i){
                    System.out.println(User.userInfo.get(i));
                }
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertBudgetInfo(MonthlyBudget_data mb_data){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("INSERT INTO budget_info(user_id, budget_name, limited_budget,expense_amount, period, init_date, " +
                        "final_date,category, color, notify1, notify2, expense_index) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
                ps.setInt(1, User.id);
                ps.setString(2,mb_data.Budget_name);
                ps.setBigDecimal(3,new java.math.BigDecimal(mb_data.limit_amount));
                ps.setBigDecimal(4,new java.math.BigDecimal(mb_data.expense_amount));
                ps.setString(5,mb_data.period);
                ps.setDate(6,java.sql.Date.valueOf(mb_data.init_date));
                ps.setDate(7,java.sql.Date.valueOf(mb_data.final_date));
                ps.setString(8,mb_data.selected_cat);
                ps.setString(9,String.valueOf(mb_data.cat_color));
                ps.setBoolean(10,mb_data.notify1);
                ps.setBoolean(11,mb_data.notify2);
                ps.setInt(12,mb_data.Expense_index);
                int ri = ps.executeUpdate();
                if(ri>0){
                    System.out.println("data inserted...");
                }
                else System.out.println("Error occured...Try again" );
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    void getBudgetInfo(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM budget_info WHERE user_id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }

                do{
                    String budget_name=rs.getString("budget_name");
                    double limit=rs.getDouble("limited_budget");
                    double expense=rs.getDouble("expense_amount");
                    String period=rs.getString("period");
                    String idate=rs.getString("init_date");
                    String fdate=rs.getString("final_date");
                    String cat=rs.getString("category");
                    String color=rs.getString("color");
                    int index=rs.getInt("expense_index");
                    boolean not1=rs.getBoolean("notify1");
                    boolean not2=rs.getBoolean("notify2");
                    User.MB_data.add(new MonthlyBudget_data(budget_name,cat,index, Color.valueOf(color),period,limit,expense,idate,fdate,not1,not2));
                    System.out.println(period);
                }while(rs.next());
                System.out.println("Budget info retrieved...!" );
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void updateFinancialExpenses(String cat, String date, Double expense){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE expense_info set " +
                        "expense=? WHERE user_id=? and category=? and idate=?");
                ps.setBigDecimal(1, new java.math.BigDecimal(expense));
                ps.setInt(2, User.id);
                ps.setString(3, cat);
                ps.setDate(4, java.sql.Date.valueOf(date));
                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("UserInfo updated..." );
                }
                else {
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO expense_info " +
                            "(user_id,category,expense,idate)"+
                            "VALUES (?,?,?,?) ");
                    ps1.setInt(1, User.id);
                    ps1.setString(2, cat);
                    ps1.setBigDecimal(3, new java.math.BigDecimal(expense));
                    ps1.setDate(4,java.sql.Date.valueOf(date));
                    int ru1 = ps1.executeUpdate();
                }
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void insertFixedDepositInfo(FixedDeposit_data fd_data){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("INSERT INTO fixed_deposit_info(user_id, bank_name, initial_saving, current_saving, maturity_amount, idate, " +
                        "fdate, notify, comp_freq, maturity_unit, maturity_duration, interest) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
                ps.setInt(1, User.id);
                ps.setString(2,fd_data.dpst_BankName);
                ps.setBigDecimal(3,new java.math.BigDecimal(fd_data.Invested));
                ps.setBigDecimal(4,new java.math.BigDecimal(fd_data.Saving_amnt));
                ps.setBigDecimal(5,new java.math.BigDecimal(fd_data.Maturity_val).setScale(4, RoundingMode.HALF_UP));
                ps.setDate(6,java.sql.Date.valueOf(fd_data.Init_date));
                ps.setDate(7,java.sql.Date.valueOf(fd_data.Final_date));
                ps.setBoolean(8,fd_data.notify);
                ps.setBigDecimal(9, new java.math.BigDecimal(fd_data.Comp_freq));
                ps.setBigDecimal(10, new java.math.BigDecimal(fd_data.Maturity_unit).setScale(4, RoundingMode.HALF_UP));
                ps.setBigDecimal(11, new java.math.BigDecimal(fd_data.Maturity_duration));
                ps.setBigDecimal(12, new java.math.BigDecimal(fd_data.interest));
                int ri = ps.executeUpdate();

                if(ri>0){
                    System.out.println("Fixed Deposit data inserted...");
                }
                else System.out.println("Error occured...Try again" );
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    void getFixedDepositInfo(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM fixed_deposit_info WHERE user_id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }
                do{
                    String bank_name=rs.getString("bank_name");
                    double invested=rs.getDouble("initial_saving");
                    double saving=rs.getDouble("current_saving");
                    double maturity_val=rs.getDouble("maturity_amount");
                    String idate=rs.getString("idate");
                    String fdate=rs.getString("fdate");
                    boolean notify=rs.getBoolean("notify");
                    double compFreq=rs.getDouble("comp_freq");
                    double maturityUnit=rs.getDouble("maturity_unit");
                    double maturityDuration=rs.getDouble("maturity_duration");
                    double interest=rs.getDouble("interest");
                    User.FD_data.add(new FixedDeposit_data(bank_name, saving, invested, maturity_val, maturity_val-invested, LocalDate.parse(idate), LocalDate.parse(fdate), notify, compFreq, maturityUnit, maturityDuration, interest));
                }while(rs.next());
                System.out.println("Fixed_deposit_info retrieved...!" );
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
