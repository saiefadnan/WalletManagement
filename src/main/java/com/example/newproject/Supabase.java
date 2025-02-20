package com.example.newproject;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

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
            }
        }catch(SQLException e){
            e.printStackTrace();
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
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
