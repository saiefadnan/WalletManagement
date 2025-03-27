package com.example.newproject;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.scene.paint.Color;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Supabase {
    //for runnind in IDE
//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String URL = dotenv.get("DB_URL");
//    private static final String USER = dotenv.get("DB_USER");
//    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    //for deploying
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static Supabase instance;
    private static Connection conn;

    public Supabase() {
        try {
            conn =  DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Supabase PostgreSQL!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Supabase getInstance(){
        System.out.println("hello"+instance);
        if(instance==null || conn==null) {
            instance= new Supabase();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();//
                System.out.println("Database connection closing....");
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
                ps.setString(5,LoginManager.hashPassword(signup[4]));
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkValidity(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT name, password FROM userinfo WHERE id=?");
                ps.setInt(1, Integer.parseInt(LoginManager.getUserID()));
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    return false;
                }
                String username = rs.getString("name");
                String fetchedPass = rs.getString("password");
                if(Objects.equals(username, LoginManager.getUsername()) && Objects.equals(fetchedPass, LoginManager.getPassword())){
                    System.out.println("User validity matched..." );
                    return true;
                }
                return false;
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
                System.out.println("checking user credentials... "+LoginManager.hashPassword(password));
                PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM userinfo WHERE name=? and password=?");
                ps.setString(1,name);
                ps.setString(2,LoginManager.hashPassword(password));
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUserInfo(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT firstname,lastname,email,gender,dob,profession,address,phone,unread_notifs,lents_count," +
                        "debts_count,goals_count FROM userinfo WHERE id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available of user ID: "+ User.id);
                    return;
                }
                    User.lents_count = Integer.parseInt(rs.getString("lents_count"));
                    User.debts_count = Integer.parseInt(rs.getString("debts_count"));
                    User.goals_count = Integer.parseInt(rs.getString("goals_count"));
                    User.userInfo.add(rs.getString("firstname"));
                    User.userInfo.add(rs.getString("lastname"));
                    User.userInfo.add(rs.getString("email"));
                    User.userInfo.add(rs.getString("gender"));
                    User.userInfo.add(rs.getString("dob"));
                    User.userInfo.add(rs.getString("profession"));
                    User.userInfo.add(rs.getString("address"));
                    User.userInfo.add(rs.getString("phone"));
                    User.temp_unread_notif = rs.getInt("unread_notifs");
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
                System.out.println(User.id);
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM financial_records WHERE user_id=?");
                ps.setInt(1, User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }
                User.Balance=rs.getDouble("balance");
                User.Expense_Cat[0]=rs.getDouble("foods & drinks");
                User.Expense_Cat[1]=rs.getDouble("shopping");
                User.Expense_Cat[2]=rs.getDouble("housing");
                User.Expense_Cat[3]=rs.getDouble("transportation");
                User.Expense_Cat[4]=rs.getDouble("vehicle");
                User.Expense_Cat[5]=rs.getDouble("life & entertainment");
                User.Expense_Cat[6]=rs.getDouble("investments");
                User.Expense_Cat[7]=rs.getDouble("income");
                User.Expense_Cat[8]=rs.getDouble("communication");
                User.Expense_Cat[9]=rs.getDouble("financial expenses");
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

    public void insertBudgetInfo(BudgetPlan mb_data){
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
                    int budget_id = rs.getInt("id");
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
                    User.MB_data.add(new BudgetPlan(budget_id, budget_name,cat,index, Color.valueOf(color),period,limit,expense,idate,fdate,not1,not2));
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
    public void insertFixedDepositInfo(FixedDeposit fd_data){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("INSERT INTO fixed_deposit_info(user_id, bank_name, initial_saving, current_saving, maturity_amount, idate, " +
                        "fdate, notify, comp_freq, maturity_unit, maturity_duration, interest) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
                ps.setInt(1, User.id);
                ps.setString(2,fd_data.dpst_BankName);
                ps.setBigDecimal(3,new java.math.BigDecimal(fd_data.Invested).setScale(4, RoundingMode.HALF_UP));
                ps.setBigDecimal(4,new java.math.BigDecimal(fd_data.Saving_amnt).setScale(4, RoundingMode.HALF_UP));
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
                    int id = rs.getInt("id");
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
                    User.FD_data.add(new FixedDeposit(id, bank_name, saving, invested, maturity_val, maturity_val-invested, LocalDate.parse(idate), LocalDate.parse(fdate), notify, compFreq, maturityUnit, maturityDuration, interest));
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
    protected boolean queryUser(String user_email) {
        try {
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE email=?");
                ps.setString(1, user_email);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    System.out.println("no data available...");
                    return false;
                }
                System.out.println("got it...");
                return true;
            }
            else{
                System.out.println("Connection error...try later");
                Main.connect_Database_On_New_thread();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    protected boolean resetPassword(String email, String new_password){
        try{
            if(conn!=null){
                String np=LoginManager.hashPassword(new_password);
                PreparedStatement ps = conn.prepareStatement("UPDATE userinfo SET" +
                        " password=? WHERE email=?");
                ps.setString(1,np);
                ps.setString(2, email);
                int ru = ps.executeUpdate();

                if(ru>0){
                    System.out.println("Password updated...");
                    return true;
                }
                else {
                    System.out.println("Error occured...Try again" );
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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void update_notify_status(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE fixed_deposit_info SET" +
                        " notify=? WHERE user_id=?");
                ps.setBoolean(1,false);
                ps.setInt(2, User.id);
                int ru = ps.executeUpdate();

                if(ru>0){
                    System.out.println("notify status updated");
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

    public void fetchNotifs(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT content FROM notifications " +
                        "WHERE user_id=?");
                ps.setInt(1,User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }

                do{
                    String notif_content=rs.getString("content");
                    Notification_data nf_data = new Notification_data(notif_content);
                    User.NF_data.add(nf_data);
                }while(rs.next());
                User.noti_num = User.NF_data.size() - User.temp_unread_notif;
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
    public void storeNotifs(String content){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("INSERT INTO notifications (user_id,content)" +
                        " VALUES(?,?)");
                ps.setInt(1,User.id);
                ps.setString(2, content);
                int ri = ps.executeUpdate();

                if(ri>0){
                    System.out.println("notification stored");
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

    public void insertExpense(String cat, Double expense){
        try{
            if(conn!=null){
                System.out.println(cat.toLowerCase());
                PreparedStatement ps = conn.prepareStatement("UPDATE financial_records set \"" +
                        cat.toLowerCase() + "\" =? , balance=? WHERE user_id=? ");

                ps.setBigDecimal(1, new java.math.BigDecimal(expense));
                ps.setDouble(2, User.Balance);
                ps.setInt(3, User.id);
                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("financial info updated..." );
                }
                else {
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO financial_records " +
                            "(user_id,balance, \"" + cat.toLowerCase() +"\")"+
                            "VALUES (?,?,?) ");
                    ps1.setInt(1, User.id);
                    ps1.setDouble(2, User.Balance);
                    ps1.setBigDecimal(3, new java.math.BigDecimal(expense));
                    int ri = ps1.executeUpdate();
                    if(ri>0){
                        System.out.println("financial info inserted..." );
                    }
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
    public void updateBalance(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE financial_records set " +
                        "balance=? WHERE user_id=? ");

                ps.setDouble(1, User.Balance);
                ps.setInt(2, User.id);
                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("balance updated..." );
                }
                else {
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO financial_records " +
                            "(user_id,balance) "+
                            "VALUES (?,?) ");
                    ps1.setInt(1, User.id);
                    ps1.setDouble(2, User.Balance);
                    int ri = ps1.executeUpdate();
                    if(ri>0){
                        System.out.println("financial info inserted..." );
                    }
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

    public void updateUnreadNotifs(int count){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE userinfo set " +
                        "unread_notifs=? WHERE id=? ");

                ps.setInt(1, count);
                ps.setInt(2, User.id);
                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("unread notifs stored as 0" );
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

    public void getExpenseByDate(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM expense_by_date WHERE user_id=? ");
                ps.setInt(1, User.id);

                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available..." );
                    return;
                }
                do{
                    Date_CategoryKey key=new Date_CategoryKey(LocalDate.parse(rs.getString("date_")),rs.getString("category"));
                    User.Expense_data.put(key, rs.getDouble("amount"));
                    //System.out.println(key);
                }while(rs.next());

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

    public void insertExpenseByDate(Date_CategoryKey key, Date_CategoryKey key1, Double val, Double val1){
        try{
            LocalDate date_ = key.date;
            String cat = key.category;
            LocalDate date_1 = key1.date;
            String cat1 = key1.category;
            System.out.println(cat);
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE expense_by_date SET " +
                        "amount = CASE " +
                        "WHEN category = ? AND date_ = ? THEN ? " +
                        "WHEN category = ? AND date_ = ? THEN ? " +
                        "ELSE amount END " +
                        "WHERE user_id=? AND (category = ? AND date_ = ? OR category = ? AND date_ = ?)");
                ps.setString(1, cat);
                ps.setDate(2, java.sql.Date.valueOf(date_));
                ps.setDouble(3, val);
                ps.setString(4, cat1);
                ps.setDate(5, java.sql.Date.valueOf(date_1));
                ps.setDouble(6, val1);
                ps.setInt(7, User.id);
                ps.setString(8, cat);
                ps.setDate(9, java.sql.Date.valueOf(date_));
                ps.setString(10, cat1);
                ps.setDate(11, java.sql.Date.valueOf(date_1));

                int ru = ps.executeUpdate();
                if(ru>=2){
                    System.out.println("expense by date updated..." );
                }
                else if (ru==1){
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO expense_by_date " +
                            "(user_id,amount,date_,category) " +
                            "VALUES (?,?,?,?)");
                    ps1.setInt(1, User.id);
                    ps1.setDouble(2, val);
                    ps1.setDate(3,java.sql.Date.valueOf(date_));
                    ps1.setString(4, cat);

                    int ri = ps1.executeUpdate();
                    if(ri>0){
                        System.out.println("expense by date inserted..." );
                    }
                }
                else{
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO expense_by_date " +
                            "(user_id,amount,date_,category) " +
                            "VALUES (?,?,?,?), (?,?,?,?)");
                    ps1.setInt(1, User.id);
                    ps1.setDouble(2, val);
                    ps1.setDate(3,java.sql.Date.valueOf(date_));
                    ps1.setString(4, cat);
                    ps1.setInt(5, User.id);
                    ps1.setDouble(6, val1);
                    ps1.setDate(7,java.sql.Date.valueOf(date_1));
                    ps1.setString(8, cat1);

                    int ri = ps1.executeUpdate();
                    if(ri>0){
                        System.out.println("expense by date inserted..." );
                    }
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

    public void getBalanceBydate(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM balance_by_date " +
                        "WHERE user_id=? AND date_ > CURRENT_DATE - INTERVAL '30 days' " +
                        "ORDER BY date_ ASC");
                ps.setInt(1,User.id);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    System.out.println("no data available...");
                    return;
                }

                do{
                    User.BalanceGraph.put(rs.getString("date_"), rs.getDouble("balance"));
                }while(rs.next());
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

    public void insertBalanceByDate(LocalDate ld){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE balance_by_date SET " +
                        "balance = ? " +
                        "WHERE user_id=? AND date_ = ?");
                ps.setDouble(1, User.Balance);
                ps.setInt(2, User.id);
                ps.setDate(3, java.sql.Date.valueOf(ld));

                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("balance by date updated..." );
                }
                else{
                    PreparedStatement ps1 = conn.prepareStatement("INSERT INTO balance_by_date " +
                            "(user_id,balance,date_) " +
                            "VALUES (?,?,?)");
                    ps1.setInt(1, User.id);
                    ps1.setDouble(2, User.Balance);
                    ps1.setDate(3,java.sql.Date.valueOf(ld));
                    int ri = ps1.executeUpdate();
                    if(ri>0){
                        System.out.println("balance by date inserted..." );
                    }
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

    public void updateBudgetInfo(BudgetPlan mb_data){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE budget_info SET " +
                        "expense_amount = ? " +
                        "WHERE user_id=? AND id=?");
                ps.setDouble(1, mb_data.expense_amount);
                ps.setInt(2, User.id);
                ps.setInt(3, mb_data.budget_id);

                int ru = ps.executeUpdate();
                if(ru>0){
                    System.out.println("budget info updated..." );
                }
                else{
                    System.out.println("budget info not found..." );
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
    public void update_budgetinfo_notify1_status(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE budget_info SET" +
                        " notify1 = ? WHERE user_id=?");
                ps.setBoolean(1,false);
                ps.setInt(2, User.id);
                int ru = ps.executeUpdate();

                if(ru>0){
                    System.out.println("notify1 status updated");
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
    public void update_budgetinfo_notify2_status(){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("UPDATE budget_info SET" +
                        " notify2 = ? WHERE user_id=?");
                ps.setBoolean(1,false);
                ps.setInt(2, User.id);
                int ru = ps.executeUpdate();

                if(ru>0){
                    System.out.println("notify2 status updated");
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

    public void deleteFixedDepositData(int id){
        try{
            if(conn!=null){
                PreparedStatement ps = conn.prepareStatement("DELETE FROM fixed_deposit_info " +
                        "WHERE user_id=? AND id=?");
                ps.setInt(1,User.id);
                ps.setInt(2, id);
                int rd = ps.executeUpdate();

                if(rd>0){
                    System.out.println("FD data deletion successful");
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

//    public static void insertGoalsCount() throws ClassNotFoundException, SQLException {
//        try{
//            if(conn!=null){
//                String sql = "INSERT INTO user_data (username,goals_count,debts_count,lents_count) VALUES (?,?,?,?)";
//                PreparedStatement stmt = conn.prepareStatement(sql);
//                stmt.setString(1,user);
//                stmt.setInt(2,0);
//                stmt.setInt(3,0);
//                stmt.setInt(4,0);
//                boolean inserted = stmt.execute();
//                if(inserted){
//                    System.out.println("Successfully debt inserted");
//                }
//            }
//            else{
//                System.out.println("Connection error...try later");
//                Main.connect_Database_On_New_thread();
//            }
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//        }
//    }

    public void insertDebt(String debt,double target,double received,LocalDate dates,String notes){
        try{
            if(conn!=null){
                String sql="INSERT INTO debts (user_id,name,debt_amount,repaid_amount,target_date,note) VALUES (?,?,?,?,?,?)";
                PreparedStatement stmt=conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                stmt.setString(2,debt);
                stmt.setDouble(3,target);
                stmt.setDouble(4,received);
                stmt.setDate(5,java.sql.Date.valueOf(dates));
                stmt.setString(6,notes);
                int inserted = stmt.executeUpdate();
                if(inserted>0){
                    System.out.println("Successfully debt inserted");
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

    public void insertLent(String lent,double target,double received,LocalDate dates,String notes){
        try{
            if(conn!=null){
                    String sql="INSERT INTO lents (user_id,name,lent_amount,rec_amount,target_date,note) VALUES (?,?,?,?,?,?)";
                    PreparedStatement stmt=conn.prepareStatement(sql);
                    stmt.setInt(1,User.id);
                    stmt.setString(2,lent);
                    stmt.setDouble(3,target);
                    stmt.setDouble(4,received);
                    stmt.setDate(5,java.sql.Date.valueOf(dates));
                    stmt.setString(6,notes);
                    boolean inserted = stmt.execute();
                    if(inserted){
                        System.out.println("Successfully lent inserted");
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

    public void insertGoal(String goal,double target,double saved,LocalDate dates,String notes){
        try{
            if(conn!=null){
                String sql="INSERT INTO goals (user_id,name,target_amount,saved_amount,target_date,note) VALUES (?,?,?,?,?,?)";
                PreparedStatement stmt=conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                stmt.setString(2,goal);
                stmt.setDouble(3,target);
                stmt.setDouble(4,saved);
                stmt.setDate(5,java.sql.Date.valueOf(dates));
                stmt.setString(6,notes);
                boolean inserted = stmt.execute();
                if(inserted){
                    System.out.println("Successfully lent inserted");
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
    public void updateDebts(String goals,double target,double saved,String name,LocalDate date, String note){
        try{
            if(conn!=null){
                String sql = "UPDATE debts SET debt_amount = ?,repaid_amount=?,target_date=?,name=?,note=? WHERE user_id = ? AND name = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1,target);
                stmt.setDouble(2,saved);
                stmt.setDate(3,java.sql.Date.valueOf(date));
                stmt.setString(4,name);
                stmt.setString(5,note);
                stmt.setInt(6,User.id);
                stmt.setString(7,goals);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("Successfully debt updated");
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

    public void updateGoals(String goals,double target,double saved,String name,LocalDate date, String note){
        try{
            if(conn!=null){
                String sql = "UPDATE goals SET target_amount = ?,saved_amount=?,target_date=?,name=?,note=? WHERE user_id = ? AND name = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1,target);
                stmt.setDouble(2,saved);
                stmt.setDate(3,java.sql.Date.valueOf(date));
                stmt.setString(4,name);
                stmt.setString(5,note);
                stmt.setInt(6,User.id);
                stmt.setString(7,goals);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("Successfully goal updated");
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

    public void updateLents(String goals,double target,double saved,String name,LocalDate date, String note){
        try{
            if(conn!=null){
                String sql = "UPDATE lents SET lent_amount = ?,rec_amount=?,target_date=?,name=?,note=? WHERE user_id = ? AND name = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1,target);
                stmt.setDouble(2,saved);
                stmt.setDate(3,java.sql.Date.valueOf(date));
                stmt.setString(4,name);
                stmt.setString(5,note);
                stmt.setInt(6,User.id);
                stmt.setString(7,goals);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("Successfully debt updated");
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

    public void deleteGoal(String s){
        try{
            if(conn!=null){
                String sql = "DELETE FROM goals WHERE user_id = ? AND name = ?";
                PreparedStatement stmt=conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                stmt.setString(2,s);
                boolean deleted = stmt.execute();
                if(deleted)System.out.println("Successfully goal deleted");
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

    public void deleteLent(String s){
        try{
            if(conn!=null){
                String sql = "DELETE FROM lents WHERE user_id = ? AND name = ?";
                PreparedStatement stmt=conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                stmt.setString(2,s);
                boolean deleted = stmt.execute();
                if(deleted)System.out.println("Successfully lent deleted");
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

    public void deleteDebt(String s){
        try{
            if(conn!=null){
                String sql = "DELETE FROM debts WHERE user_id = ? AND name = ?";
                PreparedStatement stmt=conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                stmt.setString(2,s);
                boolean deleted = stmt.execute();
                if(deleted)System.out.println("Successfully debt deleted");
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

    public void getLentsInfo(){
        try{
            if(conn!=null){
                String sql="SELECT * FROM lents WHERE user_id = ? ORDER BY id";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                ResultSet rs = stmt.executeQuery();
                if(!rs.next()){
                    System.out.println("no lents data...");
                    return;
                }
                do{
                    String txt=rs.getString("name");
                    double target=rs.getDouble("lent_amount");
                    double saved=rs.getDouble("rec_amount");
                    String date=rs.getString("target_date");
                    String notes=rs.getString("note");
                    User.ap_Lname.add(txt);
                    User.ap_lent.add(target);
                    User.ap_received.add(saved);
                    User.ap_Ldate.add(LocalDate.parse(date));
                    User.ap_Lnote.add(notes);
                }while(rs.next());
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

    public void getDebtsInfo(){
        try{
            if(conn!=null){
                String sql="SELECT * FROM debts WHERE user_id = ? ORDER BY id";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                ResultSet rs = stmt.executeQuery();
                if(!rs.next()){
                    System.out.println("no debts data...");
                    return;
                }

                do{
                    String txt=rs.getString("name");
                    double target=rs.getDouble("debt_amount");
                    double saved=rs.getDouble("repaid_amount");
                    String date=rs.getString("target_date");
                    String notes=rs.getString("note");
                    User.ap_Dname.add(txt);
                    User.ap_debt.add(target);
                    User.ap_repaid.add(saved);
                    User.ap_Ddate.add(LocalDate.parse(date));
                    User.ap_Dnote.add(notes);
                    System.out.println(txt);
                }while(rs.next());
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

        public void getGoalsInfo(){
        try{
            if(conn!=null){
                String sql="SELECT * FROM goals WHERE user_id = ? ORDER BY id";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.id);
                ResultSet rs = stmt.executeQuery();
                if(!rs.next()){
                    System.out.println("no goals data...");
                    return;
                }
                do{
                    String txt=rs.getString("name");
                    double target=rs.getDouble("target_amount");
                    double saved=rs.getDouble("saved_amount");
                    String date=rs.getString("target_date");
                    String notes=rs.getString("note");
                    User.ap_name.add(txt);
                    User.ap_target.add(target);
                    User.ap_saved.add(saved);
                    User.ap_date.add(LocalDate.parse(date));
                    User.ap_note.add(notes);
                }while(rs.next());
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
    public void updateDebtCount(){
        try{
            if(conn!=null){
                String sql = "UPDATE userinfo SET debts_count=? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.debts_count);
                stmt.setInt(2,User.id);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("debts count updated...");
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

    public void updateLentCount(){
        try{
            if(conn!=null){
                String sql = "UPDATE userinfo SET lents_count=? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.lents_count);
                stmt.setInt(2,User.id);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("lents count updated...");
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

    public void updateGoalCount(){
        try{
            if(conn!=null){
                String sql = "UPDATE userinfo SET goals_count=? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,User.goals_count);
                stmt.setInt(2,User.id);
                int pu = stmt.executeUpdate();
                if(pu>0){
                    System.out.println("goals count updated...");
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
}
