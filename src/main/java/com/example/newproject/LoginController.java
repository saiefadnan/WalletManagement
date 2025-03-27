package com.example.newproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends SQLConnection
{
    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameTextField,passTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Label LoginPrompt;
    @FXML
    private CheckBox seePass;
    @FXML
    private Button extBtn;

    public void seePassword(ActionEvent event){
        if (seePass.isSelected()) {
            passTextField.setText(enterPasswordField.getText());
            passTextField.setDisable(false);
            passTextField.setVisible(true);
            enterPasswordField.setDisable(true);
            enterPasswordField.setVisible(false);
            return;
        }
        enterPasswordField.setText(passTextField.getText());
        enterPasswordField.setDisable(false);
        enterPasswordField.setVisible(true);
        passTextField.setDisable(true);
        passTextField.setVisible(false);
    }
    public void switch3(ActionEvent event)
    {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Forget-password.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root,splashWindow.width,splashWindow.height);
            stage.setScene(scene);
            stage.show();
            //stage.setFullScreen(true);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void switch2(ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Sign-up.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
    public void exitEntered(MouseEvent event){
        extBtn.setStyle("-fx-background-color: #ff0000;");
    }
    public void exitExited(MouseEvent event){
        extBtn.setStyle("-fx-background-color: #000022;");
    }
    public void exitbtn(ActionEvent event) throws SQLException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit.");
        if (alert.showAndWait().get()== ButtonType.OK)
            System.exit(0);
    }

    public void login(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        if(usernameTextField.getText().isEmpty()) {
            LoginPrompt.setText("Please enter username.");
            return;
        }
        if(enterPasswordField.getText().isEmpty()&&passTextField.getText().isEmpty()) {
            LoginPrompt.setText("Please enter password.");
            return;
        }
        String username=usernameTextField.getText();
        String password=enterPasswordField.getText();
        String password1=passTextField.getText();
        if(!Supabase.getInstance().loginController(username, password)){
            LoginPrompt.setText("An error occured...Please try again.");
            return;
        }
        new User(username);
        LoginManager.saveLoginDetails(User.id,username,password);
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
}