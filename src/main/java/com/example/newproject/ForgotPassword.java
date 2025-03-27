package com.example.newproject;


import io.github.cdimascio.dotenv.Dotenv;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.activation.CommandMap;
import jakarta.activation.MailcapCommandMap;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

public class ForgotPassword extends SQLConnection{
    //for running in IDE
//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String app_acc = dotenv.get("APP_ACCOUNT");
//    private static final String app_pass = dotenv.get("APP_PASSWORD");

    //for deploying
    private static final String app_acc = System.getenv("APP_ACCOUNT");
    private static final String app_pass = System.getenv("APP_PASSWORD");

    private static final Pattern passwordPattern =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label cc2,userPrompt,codePrompt,rpPrompt;
    @FXML
    Button rpBtn,sendBtn,cancelBtn,submitBtn,confirmBtn1,cancelBtn1,submitBtn1;
    @FXML
    TextField userEmail, codeTextfield;
    @FXML
    PasswordField npField,cpField;
    private static int genCode;
    private static String user_email;
    public void getCode(ActionEvent event)
    {
        int code=Integer.parseInt(codeTextfield.getText());
        System.out.println(code);
        if(genCode!=code){
            codePrompt.setText("Wrong code");
            codePrompt.setTextFill(Color.RED);
            return;
        }
        codePrompt.setText("Username confirmed");
        codePrompt.setTextFill(Color.GREEN);
        rpBtn.setDisable(false);
    }
    public void switch4(ActionEvent event)
    {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
    public void switch5(ActionEvent event)
    {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Reset-password.fxml")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
    private static boolean isInternetConnectionAvailable() {
        try {
            URL url = new URL("https://www.google.com"); // or any reliable website
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }
    public void getVal(ActionEvent event) throws SQLException, ClassNotFoundException {
        Random rand=new Random();
        genCode= rand.nextInt(999999);
        user_email=userEmail.getText();
        if(!Supabase.getInstance().queryUser(user_email))
        {
            userPrompt.setText("Invalid email or user not found.");
            return;
        }
        System.out.println("email: " + user_email);
        //String email=emailQuery(username);
        String subject="Reset password";
        String body="Your confirmation code is "+genCode+". Please enter this code to reset your password." +
                "\n\n" + "Regards,\n" + "Budget Tracker Team" + "\n\n";
        if (!isInternetConnectionAvailable()) {
            userPrompt.setText("No internet connection.");
            return;
        }
        sendEmail(body,subject,user_email);
        userPrompt.setText("Confirmation code has been sent to your email account, \n check spam folder if not found");
        userPrompt.setTextFill(Color.GREEN);
        cc2.setDisable(false);
        codeTextfield.setDisable(false);
        codePrompt.setDisable(false);
        submitBtn.setDisable(false);
    }
    private static void sendEmail(String body, String subject, String email){
        String host="smtp.gmail.com";
        Properties properties=System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(app_acc, app_pass);
                }
            });
            session.setDebug(true);

            MimeMessage m = new MimeMessage(session);
            try {
                m.setFrom(new InternetAddress(app_acc));
                m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                m.setSubject(subject);
                m.setText(body);
                Transport.send(m);
                System.out.println("Sent mail successfully...");
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void resetPassword(ActionEvent event) throws SQLException, ClassNotFoundException {
        String password=npField.getText();
        String cPassword=cpField.getText();
        if(password.length()<10)
        {
            rpPrompt.setText("Password too short.");
            rpPrompt.setTextFill(Color.RED);
            return;
        }
        if(!passwordPattern.matcher(password).matches())
        {
            rpPrompt.setText("Password must contain at least one uppercase letter, one lowercase letter, \n one digit, and one special character(!@#$%^&*).");
            rpPrompt.setTextFill(Color.RED);
            return;
        }
        if(!Objects.equals(password, cPassword)) {
            rpPrompt.setText("The password information doesn't match. Please try again!");
            rpPrompt.setTextFill(Color.RED);
            return;
        }
        rpPrompt.setText("Valid.");
        rpPrompt.setTextFill(Color.GREEN);
        if(!Supabase.getInstance().resetPassword(user_email, password)){
            rpPrompt.setText("An Error occured, Try again...");
            rpPrompt.setTextFill(Color.RED);
        }
        confirmBtn1.setDisable(false);
    }
}

//123@Adnan23412432