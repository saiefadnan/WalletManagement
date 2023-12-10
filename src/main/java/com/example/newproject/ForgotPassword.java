package com.example.newproject;

import com.sun.mail.util.MailConnectException;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

public class ForgotPassword extends SQLConnection{
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
    TextField usernameTextField, codeTextfield;
    @FXML
    PasswordField npField,cpField;
    private static int genCode;
    private static String username;
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
        scene = new Scene(root);
        stage.setX(0);
        stage.setY(0);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    public void switch5(ActionEvent event)
    {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Reset-password.fxml")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setX(0);
        stage.setY(0);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
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
        username=usernameTextField.getText();
        if(!UserQuery(username))
        {
            userPrompt.setText("Invalid username or username not found.");
            return;
        }
        System.out.println("Username: " + username);
        String email=emailQuery(username);
        String subject="Reset password";
        String body="Your confirmation code is "+genCode+". Please enter this code to reset your password." +
                " If you did not request a password reset, please ignore this email. Thank you. \n\n" + "Regards,\n" + "Budget Tracker Team" +
                "\n\n" + "This is an automated email. Please do not reply to this email." + "\n\n";
        String user="budgettracker697291@gmail.com";
        if (!isInternetConnectionAvailable()) {
            userPrompt.setText("No internet connection.");
            return;
        }
        sendEmail(body,subject,email,user);
        userPrompt.setText("Confirmation code has been sent to your email account");
        userPrompt.setTextFill(Color.GREEN);
        cc2.setDisable(false);
        codeTextfield.setDisable(false);
        codePrompt.setDisable(false);
        submitBtn.setDisable(false);
    }
    private static void sendEmail(String body, String subject, String email, String user){
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
                    return new PasswordAuthentication("budgettracker697291@gmail.com",
                            "cgnbaawpqylyoskh");
                }
            });
            session.setDebug(true);

            MimeMessage m = new MimeMessage(session);
            try {
                m.setFrom(new InternetAddress(user));
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
            return;
        }
        if(!passwordPattern.matcher(password).matches())
        {
            rpPrompt.setText("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character(!@#$%^&*).");
            return;
        }
        if(!Objects.equals(password, cPassword)) {
            rpPrompt.setText("The password information doesn't match. Please try again!");
            return;
        }
        rpPrompt.setText("Valid.");
        rpPrompt.setTextFill(Color.GREEN);
        System.out.println("Username: " + username);
        updatePassword(username,password);
        confirmBtn1.setDisable(false);
    }
}
