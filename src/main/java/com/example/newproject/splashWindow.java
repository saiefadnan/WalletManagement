package com.example.newproject;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class splashWindow implements Initializable {
    @FXML
    private AnchorPane splashAp;
    public static double height=890;
    public static double width=1550;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new splash().start();
    }
    class splash extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(2500);
                Transition fadeTransition = null;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            if(!Objects.equals(LoginManager.getUserID(), "") && Supabase.getInstance().checkValidity()){
                                User.id = Integer.parseInt(LoginManager.getUserID());
                                User.Name = LoginManager.getUsername();
                                new User(User.Name);
                                System.out.println("dashboard!!!");
                                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
                            }
                            else{
                                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        Scene scene = new Scene(root, width, height);
                        Stage stage=new Stage();
                        stage.setScene(scene);
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/wallet-logo.jpg")));
                        stage.show();
                        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                            height += newHeight.doubleValue()-oldHeight.doubleValue();
                            System.out.println("Window height changed to: " + newHeight + " pixels");
                        });
                        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                            width += newWidth.doubleValue()-oldWidth.doubleValue();
                            System.out.println("Window width changed to: " + newWidth + " pixels");
                        });
                        //stage.setFullScreen(true);
                        splashAp.getScene().getWindow().hide();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
