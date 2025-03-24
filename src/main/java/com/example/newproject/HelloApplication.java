package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
            Scene scene = new Scene(root, 539, 400);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch();
    }
}
