package com.example.newproject;

import javafx.scene.paint.Color;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class SQLConnection {
    private static final String URL="jdbc:mysql://localhost:3306/project";
    private static final String USER="root";
    private static final String PASS="Sayef@312469";
    protected static void connection(String[] s) {
        Connection conn=null;
        PreparedStatement stmt=null;
        try{
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch(Exception e)
            {
                System.out.println(e);
            }
            conn= DriverManager.getConnection(URL,USER,PASS);
            String firstname=s[0];
            String lastname=s[1];
            String username=s[2];
            String email=s[3];
            String password=s[4];
            String gender=s[5];
            String dob=s[6];
            String profession=s[7];
            String address=s[8];
            String phone=s[9];

            String sql="INSERT INTO user_info (firstname,lastname,username,email,password,gender,dob,profession,address,phone) VALUES (?,?,?,?,?,?,?,?,?,?)";
            stmt=conn.prepareStatement(sql);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,username);
            stmt.setString(4,email);
            stmt.setString(5,password);
            stmt.setString(6,gender);
            stmt.setString(7,dob);
            stmt.setString(8,profession);
            stmt.setString(9,address);
            stmt.setString(10,phone);
            stmt.execute();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se)
            {
                se.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se)
            {
                se.printStackTrace();
            }
        }
    }
    public static void getInfo(String name) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM user_info WHERE username = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,name);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            User.userInfo.add(rs.getString("firstname"));
            User.userInfo.add(rs.getString("lastname"));
            User.userInfo.add(rs.getString("email"));
            User.userInfo.add(rs.getString("gender"));
            User.userInfo.add(rs.getString("dob"));
            User.userInfo.add(rs.getString("profession"));
            User.userInfo.add(rs.getString("address"));
            User.userInfo.add(rs.getString("phone"));
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public static void updateInfo() throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="UPDATE user_info SET firstname = ?,lastname = ?,email = ?,gender = ?,dob = ?,profession = ?,address = ?,phone = ? WHERE username = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,User.userInfo.get(0));
        stmt.setString(2,User.userInfo.get(1));
        stmt.setString(3,User.userInfo.get(2));
        stmt.setString(4,User.userInfo.get(3));
        stmt.setString(5,User.userInfo.get(4));
        stmt.setString(6,User.userInfo.get(5));
        stmt.setString(7,User.userInfo.get(6));
        stmt.setString(8,User.userInfo.get(7));
        stmt.setString(9,User.Name);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Info successfully updated");
    }
    public static void updateDashboard() throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="UPDATE user_dashboard SET total_balance = ?,food=?,shopping=?,housing=?,transportation=?,vehicle=?,entertainment=?,investments=?,incomes=?,communication=?,fin_expenses=?,others=? WHERE user_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setDouble(1,User.Balance);
        stmt.setDouble(2,User.Expense_Cat[0]);
        stmt.setDouble(3,User.Expense_Cat[1]);
        stmt.setDouble(4,User.Expense_Cat[2]);
        stmt.setDouble(5,User.Expense_Cat[3]);
        stmt.setDouble(6,User.Expense_Cat[4]);
        stmt.setDouble(7,User.Expense_Cat[5]);
        stmt.setDouble(8,User.Expense_Cat[6]);
        stmt.setDouble(9,User.Expense_Cat[7]);
        stmt.setDouble(10,User.Expense_Cat[8]);
        stmt.setDouble(11,User.Expense_Cat[9]);
        stmt.setDouble(12,User.Expense_Cat[10]);
        stmt.setString(13,User.Name);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Dashboard successfully updated");
    }

    public static void getDashboard(String name) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM user_dashboard WHERE user_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,name);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
        {
            User.Balance=rs.getDouble("total_balance");
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
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public static void insertDashboard(String name) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO user_dashboard (user_name,total_balance,food,shopping,housing,transportation,vehicle,entertainment,investments,incomes,communication,fin_expenses,others) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,name);
        stmt.setDouble(2,0);
        stmt.setDouble(3,0);
        stmt.setDouble(4,0);
        stmt.setDouble(5,0);
        stmt.setDouble(6,0);
        stmt.setDouble(7,0);
        stmt.setDouble(8,0);
        stmt.setDouble(9,0);
        stmt.setDouble(10,0);
        stmt.setDouble(11,0);
        stmt.setDouble(12,0);
        stmt.setDouble(13,0);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully dashboard inserted");
    }

    public static void insertFD(String name, FixedDeposit fdData) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO user_fd (user_name,bank_name,deposit_amount,invested_amount,maturity_value,earned_interest,init_date,final_date,notify,comp_freq,maturity_unit,maturity_duration,interest) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,name);
        stmt.setString(2,fdData.dpst_BankName);
        stmt.setDouble(3,fdData.Saving_amnt);
        stmt.setDouble(4,fdData.Invested);
        stmt.setDouble(5,fdData.Maturity_val);
        stmt.setDouble(6,fdData.Earned_Interest);
        stmt.setString(7,fdData.Init_date.toString());
        stmt.setString(8,fdData.Final_date.toString());
        stmt.setBoolean(9,fdData.notify);
        stmt.setDouble(10,fdData.Comp_freq);
        stmt.setDouble(11,fdData.Maturity_unit);
        stmt.setDouble(12,fdData.Maturity_duration);
        stmt.setDouble(13,fdData.interest);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully FD inserted");
    }

    public static void getFD(String name) {
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "SELECT * FROM user_fd WHERE user_name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                String bank_name=rs.getString("bank_name");
                double deposit_amount=rs.getDouble("deposit_amount");
                double invested_amount=rs.getDouble("invested_amount");
                double maturity_value=rs.getDouble("maturity_value");
                double earned_interest=rs.getDouble("earned_interest");
                String init_date=rs.getString("init_date");
                String final_date=rs.getString("final_date");
                boolean notify=rs.getBoolean("notify");
                double comp_freq=rs.getDouble("comp_freq");
                double maturity_unit=rs.getDouble("maturity_unit");
                double maturity_duration=rs.getDouble("maturity_duration");
                double interest=rs.getDouble("interest");
                User.FD_data.add(new FixedDeposit(bank_name,deposit_amount,invested_amount,maturity_value,earned_interest,LocalDate.parse(init_date),LocalDate.parse(final_date),notify,comp_freq,maturity_unit,maturity_duration,interest));
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void deleteFD(String name, FixedDeposit fdData) {
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "DELETE FROM user_fd WHERE user_name = ? AND bank_name = ? AND deposit_amount = ? AND invested_amount = ? AND maturity_value = ? AND earned_interest = ? AND init_date = ? AND final_date = ? AND notify = ? AND comp_freq = ? AND maturity_unit = ? AND maturity_duration = ? AND interest = ?";
            stmt=conn.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,fdData.dpst_BankName);
            stmt.setDouble(3,fdData.Saving_amnt);
            stmt.setDouble(4,fdData.Invested);
            stmt.setDouble(5,fdData.Maturity_val);
            stmt.setDouble(6,fdData.Earned_Interest);
            stmt.setString(7,fdData.Init_date.toString());
            stmt.setString(8,fdData.Final_date.toString());
            stmt.setBoolean(9,fdData.notify);
            stmt.setDouble(10,fdData.Comp_freq);
            stmt.setDouble(11,fdData.Maturity_unit);
            stmt.setDouble(12,fdData.Maturity_duration);
            stmt.setDouble(13,fdData.interest);
            stmt.execute();
            stmt.close();
            conn.close();
            System.out.println("Successfully deleted");
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    protected boolean query(String user, String pass) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql = "SELECT username, password FROM user_info";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            if (Objects.equals(username, user) && Objects.equals(password, pass)) {
                User.Name = username;
                return true;
            }
        }
        return false;
    }
    protected boolean UserQuery(String user) throws SQLException, ClassNotFoundException {
        boolean flag=false;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "SELECT * FROM user_info WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,user);
            ResultSet res=stmt.executeQuery();
            if(res.next()) flag=true;
            res.close();
            stmt.close();
            conn.close();
        }catch(SQLException e) {
            System.out.println(e);
        }
        return flag;
    }
    protected String emailQuery(String user) throws SQLException, ClassNotFoundException {
        String email="";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "SELECT email FROM user_info WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,user);
            ResultSet res=stmt.executeQuery();
            if(res.next()) email=res.getString("email");
            res.close();
            stmt.close();
            conn.close();
        }catch(SQLException e) {
            System.out.println(e);
        }
        return email;
    }
    protected void updatePassword(String user, String password) throws SQLException,ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql = "UPDATE user_info SET password = ? WHERE username = ?";
        stmt = conn.prepareStatement(sql);
        System.out.println(password + " Hey yo "+ user);
        stmt.setString(1,password);
        stmt.setString(2,user);
        System.out.println(password + " Hey yo "+ user);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully updated");

    }
    //Goals related
    public static void insertGoalsCount(String user) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql = "INSERT INTO user_data (username,goals_count,debts_count,lents_count) VALUES (?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setInt(2,0);
        stmt.setInt(3,0);
        stmt.setInt(4,0);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    public static int[] getCount(String user) throws ClassNotFoundException, SQLException {
        int[] count ={0,0,0};
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql = "SELECT goals_count,debts_count,lents_count FROM user_data WHERE username = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,user);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            count[0] = resultSet.getInt("goals_count");
            count[1] = resultSet.getInt("debts_count");
            count[2] = resultSet.getInt("lents_count");
        }
        resultSet.close();
        stmt.close();
        conn.close();
        return count;
    }
    public static void updateCount(String user,int gcount,int dcount,int lcount) throws SQLException,ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "UPDATE user_data SET goals_count = ?,debts_count=?,lents_count=? WHERE username = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1,gcount);
        stmt.setInt(2,dcount);
        stmt.setInt(3,lcount);
        stmt.setString(4,user);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully updated");
    }
    public static void wholeTable(String user) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM user_goals WHERE user_name = ? ORDER BY id";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,user);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
            String txt=rs.getString("goals_name");
            double target=rs.getDouble("target_amount");
            double saved=rs.getDouble("saved_amount");
            String date=rs.getString("target_date");
            String notes=rs.getString("note");
            User.ap_name.add(txt);
            User.ap_target.add(target);
            User.ap_saved.add(saved);
            User.ap_date.add(date);
            User.ap_note.add(notes);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public static void wholeTableDebt(String user) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM user_debts WHERE user_name = ? ORDER BY id";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,user);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
            String txt=rs.getString("debts_name");
            double target=rs.getDouble("debt_amount");
            double saved=rs.getDouble("repaid_amount");
            String date=rs.getString("target_date");
            String notes=rs.getString("note");
            User.ap_Dname.add(txt);
            User.ap_debt.add(target);
            User.ap_repaid.add(saved);
            User.ap_Ddate.add(date);
            User.ap_Dnote.add(notes);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public static void wholeTableLent(String user) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM user_lents WHERE user_name = ? ORDER BY id";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,user);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
            String txt=rs.getString("lents_name");
            double target=rs.getDouble("lent_amount");
            double saved=rs.getDouble("rec_amount");
            String date=rs.getString("target_date");
            String notes=rs.getString("note");
            User.ap_Lname.add(txt);
            User.ap_lent.add(target);
            User.ap_received.add(saved);
            User.ap_Ldate.add(date);
            User.ap_Lnote.add(notes);
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    public static void insertData(String user,String goal,double target,double saved,String dates,String notes) throws ClassNotFoundException, SQLException{
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO user_goals (user_name,goals_name,target_amount,saved_amount,target_date,note) VALUES (?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,goal);
        stmt.setString(3,String.valueOf(target));
        stmt.setString(4,String.valueOf(saved));
        stmt.setString(5,dates);
        stmt.setString(6,notes);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully goal inserted");
    }
    public static void insertDebt(String user,String debt,double target,double received,String dates,String notes) throws ClassNotFoundException, SQLException{
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO user_debts (user_name,debts_name,debt_amount,repaid_amount,target_date,note) VALUES (?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,debt);
        stmt.setString(3,String.valueOf(target));
        stmt.setString(4,String.valueOf(received));
        stmt.setString(5,dates);
        stmt.setString(6,notes);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully debt inserted");
    }
    public static void insertLent(String user,String lent,double target,double received,String dates,String notes) throws ClassNotFoundException, SQLException{
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO user_lents (user_name,lents_name,lent_amount,rec_amount,target_date,note) VALUES (?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,lent);
        stmt.setString(3,String.valueOf(target));
        stmt.setString(4,String.valueOf(received));
        stmt.setString(5,dates);
        stmt.setString(6,notes);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully lent inserted");
    }
    public static void deleteData(String user,String s) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "DELETE FROM user_goals WHERE user_name = ? AND goals_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,s);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully deleted");
    }
    public static void updateGoals(String goals,double target,double saved,String name,String date, String note) throws SQLException,ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "UPDATE user_goals SET target_amount = ?,saved_amount=?,target_date=?,goals_name=?,note=? WHERE user_name = ? AND goals_name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setDouble(1,target);
        stmt.setDouble(2,saved);
        stmt.setString(3,date);
        stmt.setString(4,name);
        stmt.setString(5,note);
        stmt.setString(6,User.Name);
        stmt.setString(7,goals);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully updated");
    }

    public static void updateDebts(String goals,double target,double saved,String name,String date, String note) throws SQLException,ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "UPDATE user_debts SET debt_amount = ?,repaid_amount=?,target_date=?,debts_name=?,note=? WHERE user_name = ? AND debts_name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setDouble(1,target);
        stmt.setDouble(2,saved);
        stmt.setString(3,date);
        stmt.setString(4,name);
        stmt.setString(5,note);
        stmt.setString(6,User.Name);
        stmt.setString(7,goals);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully updated");
    }
    public static void deleteDebt(String user,String s) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "DELETE FROM user_debts WHERE user_name = ? AND debts_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,s);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully deleted");
    }
    public static void updateLents(String goals,double target,double saved,String name,String date, String note) throws SQLException,ClassNotFoundException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "UPDATE user_lents SET lent_amount = ?,rec_amount=?,target_date=?,lents_name=?,note=? WHERE user_name = ? AND lents_name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setDouble(1,target);
        stmt.setDouble(2,saved);
        stmt.setString(3,date);
        stmt.setString(4,name);
        stmt.setString(5,note);
        stmt.setString(6,User.Name);
        stmt.setString(7,goals);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully updated");
    }
    public static void deleteLent(String user,String s) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL,USER,PASS);
        String sql = "DELETE FROM user_lents WHERE user_name = ? AND lents_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,s);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully deleted");
    }

    static void insertExpense(String user, String category, String date, double amount) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO expense_table (user_name,expense_type,date,expense_val) VALUES (?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        stmt.setString(2,category);
        stmt.setString(3,date);
        stmt.setDouble(4,amount);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully expense inserted");
    }

    static void updateExpense(String user, String category, String date, double amount) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="UPDATE expense_table SET expense_val = ? WHERE user_name = ? AND expense_type = ? AND date = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setDouble(1,amount);
        stmt.setString(2,user);
        stmt.setString(3,category);
        stmt.setString(4,date);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        System.out.println("Successfully expense updated");
    }
    static void retrieveExpense(String user) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM expense_table WHERE user_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,user);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
            String category=rs.getString("expense_type");
            String da=rs.getString("date");
            double amount=rs.getDouble("expense_val");
            LocalDate dat=LocalDate.parse(da);
            User.Expense_data.put(new Date_CategoryKey(dat,category),amount);
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("Successfully expense retrieved");
    }
    static boolean checkCategory(String name, String date,String cat){
        boolean flag=false;
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "SELECT * FROM expense_table WHERE user_name = ? AND date = ? AND expense_type = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,date);
            stmt.setString(3,cat);
            ResultSet res=stmt.executeQuery();
            if(res.next()) flag=true;
            res.close();
            stmt.close();
            conn.close();
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return flag;
    }

    static void insertMonthlyBudget(String name, double limit, double expense, String period, String idate, String fdate, String cat, String color, boolean not1, boolean not2, int expense_index) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="INSERT INTO monthly_budget (user_name,budget_name,limit_amount,expense_amount,period,init_date,final_date,selected_category,expense_index,category_color,notification1,notification2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,User.Name);
        stmt.setString(2,name);
        stmt.setDouble(3,limit);
        stmt.setDouble(4,expense);
        stmt.setString(5,period);
        stmt.setString(6,idate);
        stmt.setString(7,fdate);
        stmt.setString(8,cat);
        stmt.setInt(9,expense_index);
        stmt.setString(10,color);
        stmt.setBoolean(11,not1);
        stmt.setBoolean(12,not2);
        stmt.execute();
        stmt.close();
        conn.close();
        System.out.println("Successfully monthly budget inserted");
    }
    static void getMB(String name) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection(URL,USER,PASS);
        String sql="SELECT * FROM monthly_budget WHERE user_name = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setString(1,name);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        {
            int budget_id=rs.getInt("id");
            String budget_name=rs.getString("budget_name");
            double limit=rs.getDouble("limit_amount");
            double expense=rs.getDouble("expense_amount");
            String period=rs.getString("period");
            String idate=rs.getString("init_date");
            String fdate=rs.getString("final_date");
            String cat=rs.getString("selected_category");
            String color=rs.getString("category_color");
            int index=rs.getInt("expense_index");
            boolean not1=rs.getBoolean("notification1");
            boolean not2=rs.getBoolean("notification2");
            User.MB_data.add(new BudgetPlan(budget_id, budget_name,cat,index,Color.valueOf(color),period,limit,expense,idate,fdate,not1,not2));
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("Successfully monthly budget retrieved");
    }
    public static void updateMB(String budgetName, double expense, String category){
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "UPDATE monthly_budget SET expense_amount = ? WHERE user_name = ? AND budget_name = ? AND selected_category = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1,expense);
            stmt.setString(2,User.Name);
            stmt.setString(3,budgetName);
            stmt.setString(4,category);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            System.out.println("Successfully monthly budget updated");
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
    public static void updateLineChart(String name, String date, double amount) throws ClassNotFoundException, SQLException {
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "UPDATE line_chart SET amount = ? WHERE user_name = ? AND date = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1,amount);
            stmt.setString(2,name);
            stmt.setString(3,date);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            System.out.println("Successfully line chart updated");
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
    public boolean checkDate(String name, String date){
        boolean flag=false;
        Connection conn=null;
        PreparedStatement stmt=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(URL,USER,PASS);
            String sql = "SELECT * FROM line_chart WHERE user_name = ? AND date = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,date);
            ResultSet res=stmt.executeQuery();
            if(res.next()) flag=true;
            res.close();
            stmt.close();
            conn.close();
        }catch(SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return flag;
    }
}
