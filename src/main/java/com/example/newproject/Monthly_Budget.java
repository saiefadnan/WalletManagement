package com.example.newproject;

import javafx.animation.KeyFrame;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Monthly_Budget extends Abstract_controller{
    @FXML
    private ChoiceBox choicebox1;
    @FXML
    private ChoiceBox choicebox2;
    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private TextField tf1,tf2;
    @FXML
    private DatePicker datepicker,datepicker1;
    @FXML
    private Text cat,FT;
    @FXML
    private VBox vbox0,vbox1,vbox2,vbox3;
    @FXML
    private Label Brif_msg;
    private final Color[] color = {Color.BLANCHEDALMOND,Color.RED, Color.BLUE, Color.ORANGE, Color.PURPLE,Color.GREEN, Color.YELLOW,Color.DEEPPINK,Color.VIOLET,Color.TURQUOISE,Color.GRAY};
    private final String[] choice={"All","Foods & Drinks","Shopping","Housing","Transportation","Vehicle","Life & Entertainment","Investments","Communication","Financial expenses","Others"};
    private final String[] period={"One time","Weekly","Monthly","Yearly"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        choicebox1.getItems().addAll(period);
        choicebox1.setOnAction(e->choiceBox1_Action());
        choicebox2.getItems().addAll(choice);
        Init();
    }

    @Override
    public void Init(){
        for(int i=0;i< User.MB_data.size();++i){
            MonthlyBudget_data mb_data=User.MB_data.get(i);
            AnchorPane rect=new AnchorPane();
            rect.setPrefSize(730,81);
            rect.setStyle(
                    "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                            "-fx-background-radius: 20px;"
            );

            Rectangle rec=new Rectangle();
            rec.setHeight(12);
            rec.setWidth(12);
            rec.setArcWidth(8);
            rec.setArcHeight(8);
            rec.setStroke(Color.BLACK);
            rec.setStrokeWidth(1);
            rec.setFill(mb_data.cat_color);

            ProgressBar pgb=new ProgressBar();
            AnchorPane.setTopAnchor(pgb,43.0);
            AnchorPane.setLeftAnchor(pgb,25.0);
            mb_data.expense_amount=0;
            for(LocalDate date=mb_data.init_date;!date.isAfter(LocalDate.now());date=date.plus(1, ChronoUnit.DAYS)){
                Date_CategoryKey key=new Date_CategoryKey(date,choice[mb_data.Expense_index]);
                if(!User.Expense_data.containsKey(key)){
                    User.Expense_data.put(key,0.0);
                }
                mb_data.expense_amount+=User.Expense_data.get(key);
            }
            mb_data.progress=mb_data.expense_amount/mb_data.limit_amount;
            pgb.setPrefWidth(800.0);
            pgb.setProgress(mb_data.progress);

            if(mb_data.progress>.8){
                pgb.setStyle("-fx-accent: red;");
            }
            else pgb.setStyle("-fx-accent: green;");

            AnchorPane.setTopAnchor(rec,25.0);
            AnchorPane.setLeftAnchor(rec,25.0);

            Text category=new Text(mb_data.selected_cat);
            Text name=new Text("Budget Name: "+mb_data.Budget_name);
            AnchorPane.setTopAnchor(name,5.0);
            AnchorPane.setLeftAnchor(name,45.0);
            AnchorPane.setTopAnchor(category,23.0);
            AnchorPane.setLeftAnchor(category,45.0);

            Button btn=new Button("Overview");
            btn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Dynamic_Btn.css")).toExternalForm());
            btn.setOnAction(event -> {
                try {
                    graph2(event,mb_data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            AnchorPane.setTopAnchor(btn,23.0);
            AnchorPane.setRightAnchor(btn,15.0);

            rect.getChildren().addAll(rec,category,btn,pgb,name);

            if(mb_data.period.equals("One time"))
                vbox0.getChildren().add(0,rect);
            else if(mb_data.period.equals("Weekly"))
                vbox1.getChildren().add(0,rect);
            else if(mb_data.period.equals("Monthly"))
                vbox2.getChildren().add(0,rect);
            else
                vbox3.getChildren().add(0,rect);
        }

    }
    public void choiceBox1_Action(){
        if(choicebox1.getValue().equals("One time")){
            datepicker.setVisible(true);
            datepicker.setDisable(false);
            datepicker.setValue(LocalDate.now());
            datepicker.setDayCellFactory(picker ->
                    new DateCell() {
                        public void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);
                            setDisable(empty || date.isBefore(LocalDate.now()));
                        }
                    });

            datepicker1.setVisible(true);
            datepicker1.setDisable(false);
            datepicker1.setValue(LocalDate.now());
//                datepicker1.setDayCellFactory(picker ->
//                        new DateCell() {
//                            public void updateItem(LocalDate date, boolean empty) {
//                                super.updateItem(date, empty);
//                                setDisable(empty || date.isBefore(LocalDate.now()));
//                            }
//                        });

            FT.setVisible(true);
            choicebox2.setLayoutY(481);
            cat.setLayoutY(500);
        }
        else{
            datepicker.setVisible(false);
            datepicker.setDisable(true);
            datepicker1.setVisible(false);
            datepicker1.setDisable(true);
            FT.setVisible(false);
            choicebox2.setLayoutY(425);
            cat.setLayoutY(445);
        }

    }

    public void Add1() throws SQLException, ClassNotFoundException {
        MonthlyBudget_data mb_data=new MonthlyBudget_data();
        mb_data.Budget_name=tf1.getText();
        mb_data.limit_amount= Double.parseDouble(tf2.getText());
        mb_data.period= (String)choicebox1.getValue();
        if(mb_data.period.equals(period[0])){
            mb_data.final_date=datepicker.getValue();
            mb_data.init_date=datepicker1.getValue();
            mb_data.init_time=mb_data.init_date.atTime(LocalTime.of(0,0));
        }
        else if(mb_data.period.equals(period[1])){
            mb_data.final_date=LocalDate.now().plusDays(6);
            mb_data.init_date= LocalDate.now();
            mb_data.init_time=LocalDateTime.now();
        }
        else if(mb_data.period.equals(period[2])){
            mb_data.final_date=LocalDate.now().plusDays(29);
            mb_data.init_date= LocalDate.now();
            mb_data.init_time=LocalDateTime.now();
        }
        else{
            mb_data.final_date=LocalDate.now().plusDays(364);
            mb_data.init_date= LocalDate.now();
            mb_data.init_time=LocalDateTime.now();
        }
        mb_data.Today1=LocalDate.now();
        mb_data.Today2=LocalDate.now();

        for(int i=0;i<11;++i) {
            if (choicebox2.getValue() == choice[i]) {
                mb_data.selected_cat=choice[i];
                mb_data.Expense_index = i;
                break;
            }
        }
        mb_data.cat_color=color[mb_data.Expense_index];
        Date_CategoryKey key=new Date_CategoryKey(LocalDate.now(),choice[mb_data.Expense_index]);
        if(!User.Expense_data.containsKey(key)){
            User.Expense_data.put(key,0.0);
            System.out.println("Expense");
        }

        mb_data.expense_amount += User.Expense_data.get(key);
        mb_data.periodic_update= Executors.newScheduledThreadPool(1);
        mb_data.scheduledTask1=mb_data.periodic_update.scheduleAtFixedRate(()->Update_Date(mb_data), 0, 15, TimeUnit.SECONDS);

        if (checkbox1.isSelected() || checkbox2.isSelected()) {
            mb_data.notify1=checkbox1.isSelected();
            mb_data.notify2=checkbox2.isSelected();
            mb_data.Notification_checker=Executors.newScheduledThreadPool(1);
            mb_data.scheduledTask2=mb_data.Notification_checker.scheduleAtFixedRate(()->checkNotification(mb_data), 0, 15, TimeUnit.SECONDS);
        }

        mb_data.Chart_updater=Executors.newScheduledThreadPool(1);
        mb_data.scheduledTask3=mb_data.Chart_updater.scheduleAtFixedRate(()->Update_Chart(mb_data), 0, 15, TimeUnit.SECONDS);

        User.MB_data.add(mb_data);
        Add2(mb_data);
//        SQLConnection.insertMonthlyBudget(mb_data.Budget_name, mb_data.limit_amount, mb_data.expense_amount, mb_data.period,
//                String.valueOf(mb_data.init_date), String.valueOf(mb_data.final_date), mb_data.selected_cat,
//                String.valueOf(mb_data.cat_color), mb_data.notify1, mb_data.notify2, mb_data.Expense_index);
        Supabase.getInstance().insertBudgetInfo(mb_data.Budget_name, mb_data.limit_amount, mb_data.expense_amount, mb_data.period,
                mb_data.init_date, mb_data.final_date, mb_data.selected_cat,
                String.valueOf(mb_data.cat_color), mb_data.notify1, mb_data.notify2, mb_data.Expense_index);
    }

    public void checkNotification(MonthlyBudget_data mb_data) {
        System.out.println("1->"+mb_data.notify1+" "+mb_data.expense_amount+" "+mb_data.limit_amount);
        if(mb_data.notify1 && mb_data.expense_amount>=mb_data.limit_amount){
            System.out.println("1->"+mb_data.notify1+" "+mb_data.expense_amount+" "+mb_data.limit_amount);
            Platform.runLater(()->{
                Notifications.create()
                        .title("Notification!!!")
                        .text(User.Name+", Your expense amount has exceeded \nthe budget for "+choice[mb_data.Expense_index])
                        .graphic(new Label())
                        .hideAfter(Duration.seconds(9))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .showInformation();
            });
            mb_data.notify1=false;
            AddNoti(User.Name+", Your expense amount has exceeded \nthe budget for "+choice[mb_data.Expense_index]);
        }

        LocalDateTime finaltime=mb_data.final_date.atTime(LocalTime.of(23,59));
        LocalDateTime now=LocalDateTime.now();
        double time_passed=ChronoUnit.MINUTES.between(now,finaltime);
        double total_days=ChronoUnit.MINUTES.between(mb_data.init_time, finaltime);
        double avg_spending = mb_data.expense_amount / time_passed;
        double estimated_expense = avg_spending * (total_days - time_passed);
        double remaining_expense = mb_data.limit_amount - mb_data.expense_amount;
        if (mb_data.notify2 && estimated_expense > remaining_expense) {
            Platform.runLater(() -> {
                Notifications.create()
                        .title("Notification!!!")
                        .text(User.Name + ", Your expense amount is trending \nto be overspent for " + choice[mb_data.Expense_index])
                        .graphic(new Label())
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .showInformation();
            });
            mb_data.notify2=false;
            AddNoti(User.Name + ", Your expense amount is trending \nto be overspent for " + choice[mb_data.Expense_index]);
        }

        if(!mb_data.notify1 && !mb_data.notify2){
            mb_data.scheduledTask2.cancel(true);
            mb_data.Notification_checker.shutdown();
        }

    }

    public void Add2(MonthlyBudget_data mb_data){
        AnchorPane rect=new AnchorPane();
        rect.setPrefSize(730,90);
        rect.setStyle(
                "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                        "-fx-background-radius: 20px;"
        );

        Rectangle rec=new Rectangle();
        rec.setHeight(12);
        rec.setWidth(12);
        rec.setArcWidth(8);
        rec.setArcHeight(8);
        rec.setStroke(Color.BLACK);
        rec.setStrokeWidth(1);
        rec.setFill(mb_data.cat_color);
        AnchorPane.setTopAnchor(rec,25.0);
        AnchorPane.setLeftAnchor(rec,25.0);

        Text category=new Text(mb_data.selected_cat);
        Text name=new Text("Budget Name: "+mb_data.Budget_name);
        AnchorPane.setTopAnchor(category,23.0);
        AnchorPane.setLeftAnchor(category,45.0);
        AnchorPane.setTopAnchor(name,5.0);
        AnchorPane.setLeftAnchor(name,45.0);

        Button btn=new Button("Overview");
        btn.getStylesheets().add(getClass().getResource("Dynamic_Btn.css").toExternalForm());
        btn.setOnAction(event -> {
            try {
                graph2(event,mb_data);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the IOException appropriately
            }
        });
        AnchorPane.setTopAnchor(btn,23.0);
        AnchorPane.setRightAnchor(btn,5.0);

        ProgressBar pgb=new ProgressBar();
        AnchorPane.setTopAnchor(pgb,43.0);
        AnchorPane.setLeftAnchor(pgb,25.0);
        mb_data.progress=mb_data.expense_amount/mb_data.limit_amount;
        pgb.setPrefWidth(800.0);
        pgb.setProgress(mb_data.progress);
        if(mb_data.progress>.8) pgb.setStyle("-fx-accent: red;");
        else pgb.setStyle("-fx-accent: green;");

        rect.getChildren().addAll(rec,category,name,btn,pgb);

        if(mb_data.period.equals("One time"))
            vbox0.getChildren().add(0,rect);
        else if(mb_data.period.equals("Weekly"))
            vbox1.getChildren().add(0,rect);
        else if(mb_data.period.equals("Monthly"))
            vbox2.getChildren().add(0,rect);
        else
            vbox3.getChildren().add(0,rect);

        Duration duration=Duration.seconds(1);
        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    Brif_msg.setVisible(true);
                }),
                new KeyFrame(duration,e->{
                    Brif_msg.setVisible(false);
                })
        );
        timeline.play();

    }

    public void Update_Date(MonthlyBudget_data mb_data){
        if(LocalDate.now().isAfter(mb_data.final_date)){
            if(mb_data.period.equals(period[0])){
                if(mb_data.scheduledTask1!=null) {
                    mb_data.scheduledTask1.cancel(true);
                    mb_data.periodic_update.shutdown();
                }
            }
            else {
                mb_data.init_date = LocalDate.now();
                mb_data.expense_amount = 0;
                Date_CategoryKey key = new Date_CategoryKey(LocalDate.now(), choice[mb_data.Expense_index]);
                if (!User.Expense_data.containsKey(key)) {
                    User.Expense_data.put(key, 0.0);
                }
                mb_data.expense_amount += User.Expense_data.get(key);

                if (mb_data.period.equals(period[1])) {
                    mb_data.final_date = LocalDate.now().plusDays(7);
                    mb_data.init_date = LocalDate.now();
                } else if (mb_data.period.equals(period[2])) {
                    mb_data.final_date = LocalDate.now().plusDays(30);
                    mb_data.init_date = LocalDate.now();
                } else {
                    mb_data.final_date = LocalDate.now().plusDays(365);
                    mb_data.init_date = LocalDate.now();
                }
                mb_data.Today1 = LocalDate.now();
            }
        }
    }
    public void Update_Chart(MonthlyBudget_data mb_data) {
        mb_data.Ypoints[0]=mb_data.expense_amount;
        if(!mb_data.Today2.isEqual(LocalDate.now())){
            for (int i = 29; i >0; --i) {
                mb_data.Ypoints[i]=mb_data.Ypoints[i-1];
            }
        }
        mb_data.Today2=LocalDate.now();
    }

    public void AddNoti(String txt) {
        Notification_data nf_data = new Notification_data();
        nf_data.messsage.setText(txt);
        nf_data.localdate = LocalDate.now();
        nf_data.localtime = LocalTime.now();
        User.NF_data.add(nf_data);
    }

    public void graph2(ActionEvent event,MonthlyBudget_data mb_data) throws IOException {
        Budget_Overview.mb_data=mb_data;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Graph_2.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void Back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

}
