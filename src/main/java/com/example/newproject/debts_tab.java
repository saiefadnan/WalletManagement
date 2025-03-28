package com.example.newproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class debts_tab {
    public static String debts_name="";
    @FXML
    private Button btn1;
    @FXML
    private void moreInfo(ActionEvent event) throws IOException
    {
        btn1.setCursor(Cursor.HAND);
        Parent parent = btn1.getParent();
        if (parent != null) {
            AnchorPane apn= (AnchorPane) parent;
            Parent pt=apn.getParent();
            AnchorPane apTab= (AnchorPane) pt;
            for(Node node:apTab.getChildren()){
                if(node instanceof Label){
                    debts_name=((Label)node).getText();
                    break;
                }
            }
        }
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Debts info.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,splashWindow.width,splashWindow.height);
            stage.setScene(scene);
            stage.show();
            //stage.setFullScreen(true);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
