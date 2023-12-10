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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class Debts implements Initializable {
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField goalsFld,targetFld,targetFld1,noteFld;
    @FXML
    private AnchorPane debtAp,lentAp;
    @FXML
    private VBox debtBox,lentBox;
    @FXML
    private RadioButton debtCheck,lentCheck;
    @FXML
    private Label alertLabel;
    @FXML
    DatePicker dateFld;
    public static int debts_count,lents_count;

    public void initialize(URL url, ResourceBundle rb)
    {
        debts_count=User.debts_count;
        lents_count=User.lents_count;
        //System.out.println(debts_count);
        //System.out.println(lents_count);
        for(int i=0;i<debts_count;i++)
        {
            try{
                debtAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("debts_tab.fxml")));
                if(i<User.ap_Dname.size()) {
                    String s = User.ap_Dname.get(i);
                    double progress=User.ap_repaid.get(i)/User.ap_debt.get(i);
                    String d=User.ap_Ddate.get(i);
                    debtAp.getChildren().add(addLabel(s));
                    if(progress==1.0)
                        debtAp.getChildren().add(addDoneDebt());
                    else
                        debtAp.getChildren().add(addProgressBar(progress));

                    debtAp.getChildren().add(addDate(d));
                }
                debtBox.getChildren().add(debtAp);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        for(int i=0;i<lents_count;i++)
        {
            try{
                lentAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Lents_tab.fxml")));
                if(i<User.ap_Lname.size()) {
                    String s = User.ap_Lname.get(i);
                    double progress=User.ap_received.get(i)/User.ap_lent.get(i);
                    String d=User.ap_Ldate.get(i);
                    lentAp.getChildren().add(addLabel(s));
                    if(progress==1.0)
                        lentAp.getChildren().add(addDoneLent());
                    else
                        lentAp.getChildren().add(addProgressBar(progress));
                    lentAp.getChildren().add(addDate(d));
                }
                lentBox.getChildren().add(lentAp);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
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
    public Label addDoneDebt(){
        Label apLabel=new Label("Debt Repaid!");
        apLabel.setAlignment(Pos.CENTER);
        apLabel.setTextFill(Color.BLACK);
        Font customFont = Font.font("Berlin Sans FB", 20);
        apLabel.setFont(customFont);
        apLabel.setLayoutX(75);
        apLabel.setLayoutY(60);
        return apLabel;
    }
    public Label addDoneLent(){
        Label apLabel=new Label("Money Received!");
        apLabel.setAlignment(Pos.CENTER);
        apLabel.setTextFill(Color.BLACK);
        Font customFont = Font.font("Berlin Sans FB", 20);
        apLabel.setFont(customFont);
        apLabel.setLayoutX(71);
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
    private void adder(ActionEvent event) throws IOException {
        if (debtCheck.isSelected()) debtAdder();
        else if(lentCheck.isSelected()) lentAdder();
        else{
            alertLabel.setText("Please select one");
        }
    }
    @FXML
    private void debtAdder() throws IOException
    {
        try{
            debtAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("debts_tab.fxml")));
            String s=goalsFld.getText();
            String target=targetFld.getText();
            String received=targetFld1.getText();
            LocalDate dot=dateFld.getValue();
            String da=dot.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String n=noteFld.getText();
            if(s.isEmpty()||target.isEmpty()||received.isEmpty()||da.isEmpty()){
                alertLabel.setText("Please fill up the fields");
                return;
            }
            double t= Double.parseDouble(target);
            double sa= Double.parseDouble(received);
            SQLConnection.insertDebt(User.Name,s,t,sa,da,n);
            User.ap_Dname.add(s);
            User.ap_debt.add(t);
            User.ap_repaid.add(sa);
            User.ap_Ddate.add(da);
            User.ap_Dnote.add(n);
            debtAp.getChildren().add(addLabel(s));
            debtAp.getChildren().add(addProgressBar(sa/t));
            debtAp.getChildren().add(addDate(da));
            debtBox.getChildren().add(debtAp);
            TranslateTransition tt=new TranslateTransition();
            tt.setDuration(Duration.seconds(1));
            tt.setNode(debtAp);
            tt.setFromY(100);
            tt.setToY(0);
            FadeTransition ft=new FadeTransition();
            ft.setDuration(Duration.seconds(1));
            ft.setNode(debtAp);
            ft.setFromValue(0);
            ft.setToValue(1);
            tt.play();
            ft.play();
            debts_count++;
            User.debts_count=debts_count;
            //System.out.println(User.debts_count);
        }
        catch(Exception e)
        {
            alertLabel.setText("Please fill up the fields");
            System.out.println(e);
        }
    }
    @FXML
    private void lentAdder() throws IOException
    {
        try{
            lentAp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Lents_tab.fxml")));
            String s=goalsFld.getText();
            String target=targetFld.getText();
            String received=targetFld1.getText();
            LocalDate dot=dateFld.getValue();
            String da=dot.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String n=noteFld.getText();
            if(s.isEmpty()||target.isEmpty()||received.isEmpty()||da.isEmpty()){
                alertLabel.setText("Please fill up the fields");
                return;
            }
            double t= Double.parseDouble(target);
            double sa= Double.parseDouble(received);
            SQLConnection.insertLent(User.Name,s,t,sa,da,n);
            User.ap_Lname.add(s);
            User.ap_lent.add(t);
            User.ap_received.add(sa);
            User.ap_Ldate.add(da);
            User.ap_Lnote.add(n);
            lentAp.getChildren().add(addLabel(s));
            lentAp.getChildren().add(addProgressBar(sa/t));
            lentAp.getChildren().add(addDate(da));
            lentBox.getChildren().add(lentAp);
            TranslateTransition tt=new TranslateTransition();
            tt.setDuration(Duration.seconds(1));
            tt.setNode(lentAp);
            tt.setFromY(100);
            tt.setToY(0);
            FadeTransition ft=new FadeTransition();
            ft.setDuration(Duration.seconds(1));
            ft.setNode(lentAp);
            ft.setFromValue(0);
            ft.setToValue(1);
            tt.play();
            ft.play();
            lents_count++;
            User.lents_count=lents_count;
            System.out.println(User.lents_count);
        }
        catch(Exception e)
        {
            alertLabel.setText("Please fill up the fields");
            System.out.println(e);
        }
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

}
