package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUpController extends SQLConnection implements Initializable {
    private static final Pattern passwordPattern =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button nxtBtn;
    @FXML
    private Label textPrompt,passwordLabel,textPromptUser,emailPromptUser;
    @FXML
    private TextField firstName,lastName,emailField,userName,
            passwordField,confirmPasswordField,professionField,
            countryField,cityField,phoneField,passTxtFld;
    @FXML
    private DatePicker dateField;
    @FXML
    private ChoiceBox<String> genderField;
    @FXML
    private RadioButton showPass;
    private final String[] genders={"Male","Female","Other"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(genderField.getItems().isEmpty()) {
                genderField.getItems().addAll(genders);
                genderField.setOnAction(this::dataInsertion);
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    public void showPassword(ActionEvent event)
    {
        if(showPass.isSelected()) {
            passTxtFld.setText(passwordField.getText());
            passTxtFld.setDisable(false);
            passTxtFld.setVisible(true);
            passwordField.setDisable(true);
            passwordField.setVisible(false);
            return;
        }
        passwordField.setText(passTxtFld.getText());
        passwordField.setDisable(false);
        passwordField.setVisible(true);
        passTxtFld.setDisable(true);
        passTxtFld.setVisible(false);
    }
    public void switch1(ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
    public String dateChoice()
    {
        LocalDate dob=dateField.getValue();
        return dob.toString();
    }
    public void dataInsertion(ActionEvent event)
    {
        try {
            String[] signUp = new String[10];
            signUp[0] = firstName.getText();
            signUp[1] = lastName.getText();
            signUp[2] = userName.getText();
            if(signUp[2].isEmpty()){
                textPromptUser.setText("Enter a username.");
                return;
            }
            if(UserQuery(signUp[2]))
            {
                textPromptUser.setText("Username already exists. Please give a unique username.");
                return;
            }
            else {
                textPromptUser.setText("Valid username.");
                textPromptUser.setTextFill(Color.GREEN);
            }
            signUp[3] = emailField.getText();
            if(!signUp[3].matches("^(.+)@(.+)$")){
                emailPromptUser.setText("Please enter a valid email address");
                emailPromptUser.setStyle("-fx-text-fill: red");
                return;
            }
            emailPromptUser.setText("Valid email address.");
            emailPromptUser.setStyle("-fx-text-fill: green");
            String pass=passwordField.getText();
            if(pass.isEmpty()) pass=passTxtFld.getText();
            if(pass.isEmpty()) {
                passwordLabel.setText("Please enter a password.");
                passwordLabel.setTextFill(Color.RED);
                return;
            }
            String cPass=confirmPasswordField.getText();
            if(pass.length()<10) {
                passwordLabel.setText("Password is too short.");
                passwordLabel.setTextFill(Color.RED);
                return;
            }
            if(!passwordPattern.matcher(pass).matches()) {
                passwordLabel.setText("Password must contain at least one uppercase, one lowercase, one digit and one special character(!@#$%^&*).");
                passwordLabel.setTextFill(Color.RED);
                return;
            }
            if(!Objects.equals(pass, cPass)) {
                passwordLabel.setText("The password information doesn't match. Please try again!");
                passwordLabel.setTextFill(Color.RED);
                return;
            }
            signUp[4] = passwordField.getText();
            passwordLabel.setText("Valid.");
            passwordLabel.setTextFill(Color.GREEN);
            String gender=genderField.getValue();
            signUp[5]=gender;
            signUp[6] = dateChoice();
            signUp[7] = professionField.getText();
            String address=cityField.getText() + ", " + countryField.getText();
            signUp[8] = address;
            signUp[9] = phoneField.getText();
            if(signUp[9].length()!=11) {
                textPrompt.setText("Please enter a valid phone number.");
                textPrompt.setTextFill(Color.RED);
                return;
            }
            textPrompt.setText("Valid phone number.");
            textPrompt.setTextFill(Color.GREEN);
            if(!Supabase.getInstance().signupController(signUp)) {
                textPrompt.setText("Error occurred!");
                textPrompt.setTextFill(Color.RED);
                return;
            }
            //insertGoalsCount(signUp[2]);
            textPrompt.setText("All set!");
            textPrompt.setTextFill(Color.GREEN);
            //SQLConnection.insertDashboard(signUp[2]);
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root,splashWindow.width,splashWindow.height);
            stage.setScene(scene);
            stage.show();
            //stage.setFullScreen(true);
        }
        catch(Exception e) {
            System.out.println(e);
            textPrompt.setText("Please fill up all the informations.");
            textPrompt.setTextFill(Color.RED);
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), textPrompt);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event1 -> {
                fadeOutTransition.play();
            });
            pause.play();
        }
    }
}
