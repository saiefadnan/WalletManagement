package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//import static com.example.graph_2.Main.User;

public class FixedDeposit_Controller extends Abstract_controller{
    @FXML
    private ChoiceBox<String> choicebox1;
    @FXML
    private ChoiceBox<String> choicebox2;
    @FXML
    private Text Ts0,Ts1,Ts2,Ts3,Ts4,Ts5,Ts6,Ts7;
    @FXML
    private Label Brif_msg;
    @FXML
    private TextField tf1,tf2,tf3,tf4;
    @FXML
    private TextField[] tf;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane miniAnchor;
    @FXML
    private AnchorPane miniAnchor1;
    @FXML
    private DatePicker datepicker;
    @FXML
    private Arc Cir_prog;
    @FXML
    private CheckBox checkbox;
    private LocalDate Today;
    private final String[] choice1={"Monthly", "Quarterly", "Half Yearly", "Yearly"};
    private final String[] choice2={"Day(s)","Month(s)","Year(s)"};
    static boolean flag=true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tf= new TextField[]{tf1, tf2, tf3, tf4};
        choicebox1.getItems().addAll(choice1);
        choicebox2.getItems().addAll(choice2);
        Today=LocalDate.now();
        Init();
    }

    @Override
    public void Init(){
        if(flag) {
            flag=false;
            for (FixedDeposit_data fd_data : User.FD_data)
                User.net_worth += fd_data.Saving_amnt;
        }
        Ts5.setText("My current net worth: BDT "+(String.format("%.2f",User.net_worth)));
        for(int i=0;i<User.FD_data.size();++i){
            FixedDeposit_data fd_data=User.FD_data.get(i);
            AnchorPane anchorpane=new AnchorPane();
            anchorpane.setStyle(
                    "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                            "-fx-background-radius: 20px;"
            );
            anchorpane.setPrefSize(160,80);

            Text text=new Text("Bank Name: "+fd_data.dpst_BankName);
            text.setFill(Color.BLACK);
            text.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));

            Button btn=new Button("Overview");
            Button btn1=new Button("Delete");

            btn.getStylesheets().add(getClass().getResource("Dynamic_Btn.css").toExternalForm());
            btn1.getStylesheets().add(getClass().getResource("Dynamic_Btn.css").toExternalForm());
            //FixedDeposit_data fd_data=User.FD_data.get(i);
            btn.setOnAction(e->OpenOverview(fd_data));
            btn1.setOnAction(e->Deletion(anchorpane,fd_data));

            AnchorPane.setLeftAnchor(btn,0.0);
            AnchorPane.setRightAnchor(btn1,0.0);
            AnchorPane.setBottomAnchor(text,20.0);
            AnchorPane.setLeftAnchor(text,100.0);

            anchorpane.getChildren().addAll(btn,btn1,text);
            vbox.getChildren().add(anchorpane);
        }
    }
    public void Add1() throws ClassNotFoundException, SQLException {
        if (choicebox1.getValue()==null || choicebox2.getValue()==null || datepicker.getValue()==null)
            return;
        for (int i = 0; i < 4; ++i) {
            if (tf[i].getText().isEmpty()) {
                return;
            }
        }

        FixedDeposit_data fd_data=new FixedDeposit_data();
        fd_data.dpst_BankName=tf[0].getText();
        fd_data.Invested = Double.parseDouble(tf[1].getText());
        fd_data.Init_date=datepicker.getValue();
        fd_data.interest = Double.parseDouble(tf[2].getText());
        fd_data.Maturity_duration = Double.parseDouble(tf[3].getText());

        for(int i=0;i<4;++i){
            if(choicebox1.getValue().equals(choice1[i])){
                if(i==0){
                    fd_data.Comp_freq=12;
                }
                else if(i==1){
                    fd_data.Comp_freq=4;
                }
                else if(i==2){
                    fd_data.Comp_freq=2;
                }
                else {
                    fd_data.Comp_freq=1;
                }
                break;
            }
        }
        for(int i=0;i<3;++i){
            if(choicebox2.getValue().equals(choice2[i])){
                if(i==0){
                    fd_data.Maturity_unit=1.0/365;
                }
                else if(i==1){
                    fd_data.Maturity_unit=1.0/12;
                }
                else {
                    fd_data.Maturity_unit=1.0;
                }
                break;
            }
        }
        fd_data.Final_date=fd_data.Init_date.plusDays((long) (fd_data.Maturity_duration*fd_data.Maturity_unit*365));
        if(fd_data.Final_date.equals(LocalDate.now()) || fd_data.Final_date.isBefore(LocalDate.now())) {
            return;
        }
        fd_data.Maturity_val= fd_data.Invested*Math.pow((1+fd_data.interest/(100*fd_data.Comp_freq)),fd_data.Comp_freq*fd_data.Maturity_duration*fd_data.Maturity_unit);
        fd_data.Earned_Interest= fd_data.Maturity_val- fd_data.Invested;
        fd_data.Saving_amnt = current_Saving(fd_data);
        User.net_worth+=fd_data.Saving_amnt;
        //System.out.println(fd_data.Maturity_duration);

        Ts0.setText("Initial deposit date:"+fd_data.Init_date);
        Ts1.setText("BDT "+ (String.format("%.2f",fd_data.Saving_amnt)));
        Ts2.setText("You invested: BDT "+ (String.format("%.2f",fd_data.Invested)));
        Ts3.setText("Maturity Value: BDT "+ (String.format("%.2f",fd_data.Maturity_val)));
        Ts4.setText("Interest Earned: BDT "+ (String.format("%.2f",fd_data.Earned_Interest)));
        Ts5.setText("My current net worth: BDT "+(String.format("%.2f",User.net_worth)));
        Ts6.setText("Final date:"+fd_data.Final_date);
        User.FD_data.add(fd_data);

        if(checkbox.isSelected()){
            fd_data.notify=checkbox.isSelected();
            fd_data.Notification_checker= Executors.newScheduledThreadPool(1);
            fd_data.scheduledTask = fd_data.Notification_checker.scheduleAtFixedRate(()->checkTime(fd_data), 0, 15, TimeUnit.SECONDS);
        }

        Cir_ProgressBar(fd_data);
        Add2(fd_data);
        for(int i=0;i<4;++i){
            tf[i].clear();
        }
        choicebox1.setValue(null);
        choicebox2.setValue(null);
        datepicker.setValue(null);

        Duration duration=Duration.seconds(1);
        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO,e->{
                    Brif_msg.setVisible(true);
                }),
                new KeyFrame(duration,e->{
                    Brif_msg.setVisible(false);
                })
        );
        timeline.play();
        Supabase.getInstance().insertFixedDepositInfo(fd_data);
        //SQLConnection.insertFD(User.Name,fd_data);
        Close();
    }
    public void Add2(FixedDeposit_data fd_data){
        AnchorPane anchorpane=new AnchorPane();
        anchorpane.setStyle(
                "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                        "-fx-background-radius: 20px;"
        );
        anchorpane.setPrefSize(160,80);

        Text text=new Text("Bank Name: "+fd_data.dpst_BankName);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));

        Button btn=new Button("Overview");
        btn.getStylesheets().add(getClass().getResource("Dynamic_Btn.css").toExternalForm());
        btn.setOnAction(e->OpenOverview(fd_data));

        Button btn1=new Button("Delete");
        btn1.getStylesheets().add(getClass().getResource("Dynamic_Btn.css").toExternalForm());
        btn1.setOnAction(e->Deletion(anchorpane,fd_data));

        AnchorPane.setLeftAnchor(btn,0.0);
        AnchorPane.setRightAnchor(btn1,0.0);
        AnchorPane.setBottomAnchor(text,10.0);
        AnchorPane.setLeftAnchor(text,50.0);
        anchorpane.getChildren().addAll(btn,btn1,text);
        vbox.getChildren().add(0,anchorpane);

        Duration duration=Duration.seconds(1);
        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO,e->{
                    Brif_msg.setVisible(true);
                }),
                new KeyFrame(duration,e->{
                    Brif_msg.setVisible(false);
                })
        );
        timeline.play();
    }

    public double current_Saving(FixedDeposit_data fd_data){
        //System.out.println(fd_data.Comp_freq+" "+fd_data.interest+" "+fd_data.Maturity_unit+" "+fd_data.Maturity_val);
        //System.out.println(ChronoUnit.DAYS.between(fd_data.Init_date,LocalDate.now()));
        if(fd_data.Maturity_unit==1.0/365) {
            return fd_data.Invested * Math.pow((1 + fd_data.interest / (100 * fd_data.Comp_freq)), fd_data.Comp_freq * (ChronoUnit.DAYS.between(fd_data.Init_date, LocalDate.now()) * fd_data.Maturity_unit));
        }
        else if(fd_data.Maturity_unit==1.0/12) {
            return fd_data.Invested * Math.pow((1 + fd_data.interest / (100 * fd_data.Comp_freq)), fd_data.Comp_freq * (ChronoUnit.MONTHS.between(fd_data.Init_date, LocalDate.now()) * fd_data.Maturity_unit));
        }
        else {
            return fd_data.Invested * Math.pow((1 + fd_data.interest / (100 * fd_data.Comp_freq)), fd_data.Comp_freq * (ChronoUnit.YEARS.between(fd_data.Init_date, LocalDate.now()) * fd_data.Maturity_unit));
        }

    }
    public void checkTime(FixedDeposit_data fd_data){
        if(fd_data.Saving_amnt!=current_Saving(fd_data)) {
            fd_data.Saving_amnt = current_Saving(fd_data);
        }
        if(LocalDate.now().equals(fd_data.Final_date) || LocalDate.now().isAfter(fd_data.Final_date)) {
            showNotification(fd_data);
        }
        if(!Today.isEqual(LocalDate.now())) {
            Cir_ProgressBar(fd_data);
            Today=LocalDate.now();
        }
    }
    public void showNotification(FixedDeposit_data fd_data){
        Platform.runLater(()->{
                Notifications.create()
                        .title("Notification!!!")
                        .text(fd_data.dpst_BankName+": " + User.Name + ",your maturity period is over !!!"
                                + " You have \nsuccessfully deposited a total amount of \nBDT "
                                + (String.format("%.2f",fd_data.Maturity_val))+"\n\n")
                        .graphic(new Label())
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .showInformation();
        });

        Notification_data nf_data=new Notification_data();
        nf_data.messsage.setText(fd_data.dpst_BankName+"\n" + User.Name + ",your maturity period is over !!!"
                + " You \nhave successfully deposited a total amount of \nBDT "
                + (String.format("%.2f",fd_data.Maturity_val))+"\n\n");
        nf_data.localdate=LocalDate.now();
        nf_data.localtime= LocalTime.now();
        User.NF_data.add(nf_data);

        fd_data.notify=false;
        fd_data.scheduledTask.cancel(true);
        fd_data.Notification_checker.shutdown();
    }

    public void OpenOverview(FixedDeposit_data fd_data){
        Ts0.setText("Initial deposit date: "+fd_data.Init_date);
        Ts1.setText("BDT "+ (String.format("%.2f",fd_data.Saving_amnt)));
        Ts2.setText("You Invested: BDT "+(String.format("%.2f",fd_data.Invested)));
        Ts3.setText("Maturity Value: BDT "+(String.format("%.2f",fd_data.Maturity_val)));
        Ts4.setText("Interest Earned: BDT "+(String.format("%.2f",fd_data.Earned_Interest)));
        Ts5.setText("My current net worth: BDT "+(String.format("%.2f",User.net_worth)));
        Ts6.setText("Final date: "+fd_data.Final_date);
        Cir_ProgressBar(fd_data);
    }

    public void Deletion(AnchorPane anchorpane,FixedDeposit_data fd_data){
        vbox.getChildren().remove(anchorpane);

        if(fd_data.scheduledTask!=null) {
            fd_data.scheduledTask.cancel(true);
            fd_data.Notification_checker.shutdown();
        }

        Iterator<FixedDeposit_data> iterator = User.FD_data.iterator();
        while (iterator.hasNext()) {
            FixedDeposit_data item = iterator.next();
            if (item.equals(fd_data)) {
                iterator.remove();
                break;
            }
        }
        User.net_worth-=fd_data.Saving_amnt;
        Ts0.setText("Initial deposit date:");
        Ts1.setText("BDT "+ 0.0);
        Ts2.setText("You invested: BDT "+ 0.0);
        Ts3.setText("Maturity Value: BDT "+ 0.0);
        Ts4.setText("Interest Earned: BDT "+ 0.0);
        Ts5.setText("My current net worth: BDT "+(String.format("%.2f",User.net_worth)));
        Ts6.setText("Final date:");
        Cir_ProgressBar();
        SQLConnection.deleteFD(User.Name,fd_data);
    }
    public void Cir_ProgressBar(FixedDeposit_data fd_data){
        double DaysPassed = ChronoUnit.DAYS.between(fd_data.Init_date,LocalDate.now());
        double TotalDays= ChronoUnit.DAYS.between(fd_data.Init_date,fd_data.Final_date);
        double Angle;
        double progress;

        if(TotalDays!=0) {
            Angle = (DaysPassed / TotalDays) * 360;
            progress = (DaysPassed / TotalDays) * 100;
        }
        else{
            Angle=360.0;
            progress=100.0;
        }

        Ts7.setText(String.format("%.2f", progress)+"%");
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(Cir_prog.lengthProperty(), Angle))
            );
            timeline.play();
        });

    }
    public void Cir_ProgressBar(){
        double Angle=0;
        double progress=0;

        Ts7.setText("0%");
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(Cir_prog.lengthProperty(), Angle))
            );
            timeline.play();
        });

    }



    public void Back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    public void miniScene(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), miniAnchor);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        miniAnchor.setDisable(false);
        miniAnchor.setVisible(true);
    }
    public void Recordscene(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), miniAnchor1);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        miniAnchor1.setDisable(false);
        miniAnchor1.setVisible(true);
    }
    public void Close1(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), miniAnchor1);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
        miniAnchor1.setDisable(true);
        miniAnchor1.setVisible(false);
    }
    public void Close(){
        System.out.println("closing...");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), miniAnchor);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
        miniAnchor.setDisable(true);
        miniAnchor.setVisible(false);
    }
}
