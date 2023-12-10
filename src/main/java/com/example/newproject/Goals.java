package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class Goals implements Initializable{
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox goalsBox;
    @FXML
    private AnchorPane view;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private AnchorPane tabAp;
    @FXML
    private Button btn1,btn2,btn3,btn4,btn5,confirmBtn,cancelbtn;
    @FXML
    private TextField goalsFld,targetFld,savedFld,noteFld;
    @FXML
    private DatePicker dateFld;
    @FXML
    private Label warnLabel;
    public static int count;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count=User.goals_count;
        try{
            for(int i=0;i<count;i++)
            {
                try{
                    tabAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("goalsTab.fxml")));
                    if(i<User.ap_name.size()) {
                        String s = User.ap_name.get(i);
                        double progress=User.ap_saved.get(i)/User.ap_target.get(i);
                        String d=User.ap_date.get(i);
                        tabAp.getChildren().add(addLabel(s));
                        if(progress==1.0)
                            tabAp.getChildren().add(addDone());
                        else
                            tabAp.getChildren().add(addProgressBar(progress));
                        tabAp.getChildren().add(addDate(d));
                    }
                    goalsBox.getChildren().add(tabAp);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public Label addLabel(String s){
        Label apLabel=new Label(s);
        apLabel.setAlignment(Pos.CENTER);
        apLabel.setTextFill(Color.WHITE);
        Font customFont = Font.font("Berlin Sans FB", 20);
        apLabel.setFont(customFont);
        apLabel.setLayoutX(21);
        apLabel.setLayoutY(13);
        return apLabel;
    }
    public Label addDone(){
        Label apLabel=new Label("Goal Reached!");
        apLabel.setAlignment(Pos.CENTER);
        apLabel.setTextFill(Color.GREEN);
        Font customFont = Font.font("Berlin Sans FB", 20);
        apLabel.setFont(customFont);
        apLabel.setLayoutX(73);
        apLabel.setLayoutY(60);
        return apLabel;
    }
    public Label addDate(String s){
        Label apLabel=new Label(s);
        apLabel.setAlignment(Pos.CENTER);
        apLabel.setTextFill(Color.WHITE);
        Font customFont = Font.font("Berlin Sans FB", 14);
        apLabel.setFont(customFont);
        apLabel.setLayoutX(21);
        apLabel.setLayoutY(35);
        return apLabel;
    }
    public ProgressBar addProgressBar(double val){
        ProgressBar pb=new ProgressBar();
        pb.setProgress(val);
        pb.setLayoutX(20);
        pb.setLayoutY(57);
        pb.getStyleClass().add("custom-progress-bar");
        return pb;
    }
    @FXML
    private void backBtn(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void anchorPaneAdder(ActionEvent event) throws IOException
    {
        try{
            tabAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("goalsTab.fxml")));
            String s=goalsFld.getText();
            String target=targetFld.getText();
            String saved=savedFld.getText();
            LocalDate dot=dateFld.getValue();
            String da=dot.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String n=noteFld.getText();
            if(s.isEmpty()||target.isEmpty()||saved.isEmpty()||da.isEmpty()){
                warnLabel.setText("Please fill up the fields");
                return;
            }
            if(Double.parseDouble(target)<=0||Double.parseDouble(saved)<0){
                warnLabel.setText("Please enter valid values");
                return;
            }
            if(dot.isBefore(LocalDate.now())){
                warnLabel.setText("Please enter valid date");
                return;
            }
            double t= Double.parseDouble(target);
            double sa= Double.parseDouble(saved);
            if(sa>t){
                warnLabel.setText("Saved amount cannot be greater than target amount");
                return;
            }
            SQLConnection.insertData(User.Name,s,t,sa,da,n);
            User.ap_name.add(s);
            User.ap_target.add(t);
            User.ap_saved.add(sa);
            User.ap_date.add(da);
            User.ap_note.add(n);
            tabAp.getChildren().add(addLabel(s));
            tabAp.getChildren().add(addProgressBar(sa/t));
            tabAp.getChildren().add(addDate(da));
            goalsBox.getChildren().add(tabAp);
            TranslateTransition tt=new TranslateTransition();
            tt.setDuration(Duration.seconds(1));
            tt.setNode(tabAp);
            tt.setFromY(100);
            tt.setToY(0);
            FadeTransition ft=new FadeTransition();
            ft.setDuration(Duration.seconds(1));
            ft.setNode(tabAp);
            ft.setFromValue(0);
            ft.setToValue(1);
            tt.play();
            ft.play();
            count++;
            User.goals_count=count;
            System.out.println(User.goals_count);
            goalsFld.setText("");
            targetFld.setText("");
            savedFld.setText("");
            dateFld.setValue(null);
            noteFld.setText("");
        }
        catch(Exception e)
        {
            warnLabel.setText("Please fill up the fields");
        }
    }
}
