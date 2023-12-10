package com.example.newproject;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import static com.example.graph_2.Main.User;

public class DashBrd_Controller extends Abstract_controller{
    @FXML
    private Arc arc1, arc2, arc3, arc4, arc5, arc6,arc7,arc8,arc9,arc10,arc11;
    @FXML
    private TextField Tf6, Tb;
    @FXML
    private LineChart linechart;
    @FXML
    private ChoiceBox<String> choicebox;
    @FXML
    private AnchorPane miniAnchor,Noti_pane;
    @FXML
    private Text text1,text2,text3;
    @FXML
    private HBox hbox1,hbox2,hbox3,hbox4;
    @FXML
    private VBox vbox;
    @FXML
    private Label Noti_label;
    @FXML
    private Label Amount_label;
    private final Set<Color> addedCol=new HashSet<>();
    private DateTimeFormatter dateFormatter;
    private XYChart.Series<String, Number> series;
    private String today;
    private Arc[] pieC;
    private final Color[] color = {Color.RED, Color.BLUE, Color.ORANGE, Color.PURPLE,Color.GREEN, Color.YELLOW,Color.DEEPPINK,Color.YELLOWGREEN,Color.VIOLET,Color.TURQUOISE,Color.GRAY};
    private final String[] choice={"Foods & Drinks","Shopping","Housing","Transportation","Vehicle","Life & Entertainment","Investments","Income","Communication","Financial expenses","Others"};
    private final String[] choice1={"All","Foods & Drinks","Shopping","Housing","Transportation","Vehicle","Life & Entertainment","Investments","Communication","Financial expenses","Others"};
    private double z, y;
    private static boolean flag=true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pieC=new Arc[]{arc1, arc2, arc3, arc4, arc5, arc6,arc7,arc8,arc9,arc10,arc11};
        if(choicebox != null) {
            choicebox.getItems().addAll(choice);
        }
        if (linechart != null) {
            Init();
        }
        if(flag) {
            System.out.println("Brooooooooooo");

            for (int i = 0; i < 11; i++)
                User.SUM += User.Expense_Cat[i];
            System.out.println(User.SUM);
            System.out.println(User.Balance);
            System.out.println(flag);
            if(User.SUM!=0) {
                Tb.setText(Double.toString(User.Balance));
                z = y = 0;
                for (int i = 0; i < 11; ++i) {
                    pieC[i].setStartAngle(z += y);
                    y = (User.Expense_Cat[i] / User.SUM) * 360;
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(1), new KeyValue(pieC[i].lengthProperty(), y))
                    );
                    final int indx = i;
                    timeline.setOnFinished(event -> {
                        pieC[indx].setFill(color[indx]);
                        pieC[indx].setType(ArcType.ROUND);
                        update_data();
                    });
                    timeline.play();
                    if (!addedCol.contains(color[i]) && User.Expense_Cat[i] > 0) {
                        setRect(i);
                    }
                }
            }
            flag=false;
        }
        update_data();
    }

    @Override
    public void Init() {
        initchart();
        customizeChart();
        returnUI();
        schedule_();
    }

    public void enter_balance(KeyEvent e){
        if(e.getCode()== KeyCode.ENTER) {
            if (!Tb.getText().isEmpty()) {
                User.Balance = Double.parseDouble(Tb.getText());
            }
            if (User.Balance < 0) {
                User.Balance = 0.0;
            }
            if (User.Balance > 0) {
                Tf6.setDisable(false);
                User.storeY[series.getData().size() - 1] = User.Balance;
                text3.setText("BDT "+ User.Balance);
                update_data();
            } else {
                Tf6.setDisable(true);
            }
        }
    }

    public void pie(KeyEvent e) throws SQLException, ClassNotFoundException {
        if(e.getCode()==KeyCode.ENTER && !choicebox.getValue().isEmpty() && !Tf6.getText().isEmpty()) {
            double val = Double.parseDouble(Tf6.getText());
            if((!choicebox.getValue().equals("Income") && User.Balance>=val && val>0) || (choicebox.getValue().equals("Income") && val>0)){//insert db
                User.SUM+=val;
                int l=0;
                for(int i=0;i<11;++i){
                    if(choicebox.getValue().equals(choice[i])){
                        l=i;
                        break;
                    }
                }
                if(choicebox.getValue().equals("Income")){
                    User.Balance+=val;
                }
                else {
                    User.Balance-=val;
                }
                User.Expense_Cat[l]+=val;
                User.storeY[series.getData().size() - 1] = User.Balance;
                text3.setText("BDT "+ User.Balance);

                Date_CategoryKey key=new Date_CategoryKey(LocalDate.now(),choice[l]);
                Date_CategoryKey key1=new Date_CategoryKey(LocalDate.now(),"All");
                if(!User.Expense_data.containsKey(key)){
                    User.Expense_data.put(key,0.0);
                }
                if(!User.Expense_data.containsKey(key1)){
                    User.Expense_data.put(key1,0.0);
                }
                double sum_val=User.Expense_data.get(key)+val;
                double sum_val1=User.Expense_data.get(key1)+val;
                User.Expense_data.put(key,sum_val);
                User.Expense_data.put(key1,sum_val1);
                if(SQLConnection.checkCategory(User.Name,String.valueOf(LocalDate.now()),choice[l])){
                    SQLConnection.updateExpense(User.Name,choice[l], String.valueOf(LocalDate.now()),sum_val);
                    //System.out.println(sum_val+ "   Something updated!!!!");
                    //System.out.println(LocalDate.now());
                }
                else{
                    SQLConnection.insertExpense(User.Name,choice[l], String.valueOf(LocalDate.now()),sum_val);
                    //System.out.println(sum_val+"   Something inserted!!!!");
                    //System.out.println(LocalDate.now());
                }

                Platform.runLater(() -> {
                    Tb.setText(Double.toString(User.Balance));
                    Tf6.clear();
                    z=y=0;
                    for (int i = 0; i < 11; ++i) {
                        pieC[i].setStartAngle(z += y);
                        y = (User.Expense_Cat[i] / User.SUM) * 360;
                        Timeline timeline = new Timeline(
                             new KeyFrame(Duration.seconds(1), new KeyValue(pieC[i].lengthProperty(), y))
                        );
                        final int indx=i;
                        timeline.setOnFinished(event->{
                            pieC[indx].setFill(color[indx]);
                            pieC[indx].setType(ArcType.ROUND);
                            update_data();
                        });
                        timeline.play();
                        if(!addedCol.contains(color[i]) && User.Expense_Cat[i]>0) {
                            setRect(i);
                        }
                    }
                });
                Expense_Amount_Update();
            }
            else if (User.SUM == 0) {
                LinearGradient paint = new LinearGradient(
                        0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE,
                        new Stop(0.0, new Color(0.1922, 0.3216, 0.8392, 1.0)),
                        new Stop(1.0, new Color(0.1216, 1.0, 0.4863, 1.0)));
                Platform.runLater(() -> {
                    for (int i = 0; i < 6; ++i) {
                        pieC[i].setStartAngle(z += y);
                        y = 60;
                        pieC[i].setFill(paint);
                        pieC[i].setType(ArcType.ROUND);
                    }
                });
            }
        }
    }

    public void update_data() {
        Platform.runLater(()->{
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(.5), new KeyValue(series.getData().get(series.getData().size() - 1).YValueProperty(), User.Balance))
            );
            timeline.play();
        });
    }

    public void Mouse_Entered(MouseEvent e) {
        Platform.runLater(()->{
            double theta=0.0;
            for(int i=0;i<11;++i) {
                if (e.getSource() == pieC[i]) {
                    pieC[i].setRadiusX(140);
                    pieC[i].setRadiusY(140);
                    if (User.SUM > 0) {
                        theta = pieC[i].getStartAngle()+180*User.Expense_Cat[i]/User.SUM;
                        Amount_label.setText("BDT " + User.Expense_Cat[i]);
                        Amount_label.setVisible(true);
                        //Amount_label.setDisable(false);
                        double newX=1249 + 170 * Math.cos(Math.toRadians(theta));
                        double newY=300 - 170 * Math.sin(Math.toRadians(theta));
                        Amount_label.setLayoutX(newX - Amount_label.getWidth() / 2);
                        Amount_label.setLayoutY(newY - Amount_label.getHeight() / 2);
                    }
                    break;
                }
            }
        });
    }

    public void Mouse_Exited(MouseEvent e) {
        Platform.runLater(()->{
            for(int i=0;i<11;++i) {
                if(e.getSource()==pieC[i]) {
                    pieC[i].setRadiusX(130);
                    pieC[i].setRadiusY(130);
                    Amount_label.setText("");
                    Amount_label.setVisible(false);
                    //Amount_label.setDisable(true);
                    break;
                }
            }
        });
    }

    public void schedule_() {
        if (User.scheduler == null || User.scheduler.isShutdown()) {
            User.scheduler = Executors.newScheduledThreadPool(1);
            User.scheduler.scheduleAtFixedRate(this::checkUpdateChart, 0, 1, TimeUnit.SECONDS);
        } else {
            System.out.println("Scheduling already in progress.");
        }
        for(int i=0;i<User.MB_data.size();++i) {
            MonthlyBudget_data mb_data = User.MB_data.get(i);
            Monthly_Budget mb = new Monthly_Budget();
            if ((mb_data.Notification_checker == null || mb_data.Notification_checker.isShutdown()) && (mb_data.notify1 || mb_data.notify2)) {
                mb_data.Notification_checker = Executors.newScheduledThreadPool(1);
                mb_data.scheduledTask2 = mb_data.Notification_checker.scheduleAtFixedRate(() -> mb.checkNotification(mb_data), 0, 15, TimeUnit.SECONDS);
                //System.out.println("sucessfully->t2");
            }
            else{
                System.out.println("No schedule->t2");
            }
            if (mb_data.periodic_update == null || mb_data.periodic_update.isShutdown()){
                mb_data.periodic_update = Executors.newScheduledThreadPool(1);
                mb_data.scheduledTask1 = mb_data.periodic_update.scheduleAtFixedRate(() -> mb.Update_Date(mb_data), 0, 15, TimeUnit.SECONDS);
                //System.out.println("sucessfully->t1");
            }
            else{
                System.out.println("No schedule->t1");
            }
            if (mb_data.Chart_updater == null || mb_data.Chart_updater.isShutdown()){
                mb_data.Chart_updater = Executors.newScheduledThreadPool(1);
                mb_data.scheduledTask3 = mb_data.Chart_updater.scheduleAtFixedRate(() -> mb.Update_Chart(mb_data), 0, 15, TimeUnit.SECONDS);
                //System.out.println("sucessfully->t3");
            }
            else{
                System.out.println("No schedule->t3");
            }
        }
        for(int i=0;i<User.FD_data.size();++i) {
            FixedDeposit_data fd_data = User.FD_data.get(i);
            FixedDeposit_Controller fd = new FixedDeposit_Controller();
            if ((fd_data.Notification_checker == null || fd_data.Notification_checker.isShutdown()) && fd_data.notify) {
                fd_data.Notification_checker= Executors.newScheduledThreadPool(1);
                fd_data.scheduledTask = fd_data.Notification_checker.scheduleAtFixedRate(()->fd.checkTime(fd_data), 0, 15, TimeUnit.SECONDS);
                //System.out.println("sucessfully->t4");
            }else {
                System.out.println("No schedule->t4");
            }
        }
    }

    public void checkUpdateChart() {
        String currentDate = LocalDate.now().format(dateFormatter);
        if (!currentDate.equals(today)) {
            updateChart();
            today = currentDate;
        }
        if(User.NF_data.size()>User.noti_num){
            Task<Void>task= new Task<>() {
                @Override
                protected Void call() {
                    Notipane_update();
                    return null;
                }
            };

            Thread thread=new Thread(task);
            thread.start();
            task.setOnSucceeded(e->{
                System.out.println("Task completed successfully");
            });
        }
    }

    public void updateChart() {
        double[] y = new double[30];
        for (int i = 1; i < series.getData().size(); ++i) {
            XYChart.Data<String, Number> nextDataPoint = series.getData().get(i);
            y[i] = nextDataPoint.getYValue().doubleValue();
            User.storeY[i-1]=y[i];
        }
        User.storeY[series.getData().size()-1]= User.Balance;
        Platform.runLater(() -> {
            series.getData().clear();
            for (int i = 29, j = 1; i >= 0 && j <= 30; i --, j++){
                if (i == 0)
                    series.getData().add(new XYChart.Data<>("Today", User.Balance));
                else
                    series.getData().add(new XYChart.Data<>(LocalDate.now().minusDays(i).format(dateFormatter), y[j]));
            }
        });
    }

    public void Notipane_update(){
        Platform.runLater(()-> {
            User.noti_counter += (User.NF_data.size() - User.noti_num);
            Noti_label.setVisible(true);
            Noti_label.setDisable(false);
            Noti_label.setText(String.valueOf(User.noti_counter));
            System.out.println("Update!!!");

            for (int i = User.noti_num; i < User.NF_data.size(); ++i) {
                AnchorPane anchorpane = new AnchorPane();
                anchorpane.setStyle(
                        "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                                "-fx-background-radius: 20px;"
                );
                AnchorPane.setTopAnchor(User.NF_data.get(i).messsage, 20.0);
                AnchorPane.setLeftAnchor(User.NF_data.get(i).messsage, 10.0);
                anchorpane.setPrefSize(120, 80);
                anchorpane.getChildren().add(User.NF_data.get(i).messsage);
                vbox.getChildren().add(1,anchorpane);
            }
            vbox.requestLayout();
            User.noti_num=User.NF_data.size();
        });
    }

    public void initchart() {
        series = new XYChart.Series<>();
        dateFormatter = DateTimeFormatter.ofPattern("dd MMM");
        today = LocalDate.now().format(dateFormatter);
        for (int i = 29; i >= 0; i--) {
            if (i == 0)
                series.getData().add(new XYChart.Data<>("Today", 0));
            else
                series.getData().add(new XYChart.Data<>(LocalDate.now().minusDays(i).format(dateFormatter), 0));
        }
        linechart.getData().add(series);
        linechart.setPrefSize(500, 400);
    }

    public void customizeChart(){
        series.setName("Balance Trend");
        series.getNode().setStyle("-fx-stroke: green;");
        String backgroundColor = "linear-gradient(to bottom, #3152d6 0.0%, #ff2121e0 100.0%)";
        String plotBackgroundStyle = "-fx-background-color: " + backgroundColor + ";";
        linechart.lookup(".chart-plot-background").setStyle(plotBackgroundStyle);
    }

    public void Expense_Amount_Update(){
        for(int i=0;i< User.MB_data.size();++i) {
            MonthlyBudget_data mb_data = User.MB_data.get(i);
            mb_data.expense_amount = 0;
            for (LocalDate date = mb_data.init_date; !date.isAfter(LocalDate.now()); date = date.plusDays(1)) {
                Date_CategoryKey key = new Date_CategoryKey(date, choice1[mb_data.Expense_index]);
                if (!User.Expense_data.containsKey(key)) {
                    User.Expense_data.put(key, 0.0);
                }
                mb_data.expense_amount += User.Expense_data.get(key);
                SQLConnection.updateMB(mb_data.Budget_name, mb_data.expense_amount,mb_data.selected_cat);
            }
        }
    }

    public void setRect(int indx){
        Platform.runLater(()->{
            Text text = new Text(choice[indx]);
            text.setFill(Color.WHITE);

            Rectangle rect = new Rectangle();
            rect.setHeight(12);
            rect.setWidth(12);
            rect.setArcWidth(8);
            rect.setArcHeight(8);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
            rect.setFill(color[indx]);
            rect.setOnMouseEntered(event -> {
                rect.setHeight(16);
                rect.setWidth(16);
            });
            rect.setOnMouseExited(event -> {
                rect.setHeight(12);
                rect.setWidth(12);
            });

            if (hbox1.getChildren().size() < 6) hbox1.getChildren().addAll(rect, text);
            else if (hbox2.getChildren().size() < 6) hbox2.getChildren().addAll(rect, text);
            else if (hbox3.getChildren().size() < 6) hbox3.getChildren().addAll(rect, text);
            else hbox4.getChildren().addAll(rect, text);
            addedCol.add(color[indx]);
        });
    }

    public void returnUI() {
        text1.setText("Welcome Back "+User.Name+"!");
        text2.setText(User.Name);
        text3.setText("BDT "+ User.Balance);
        if(User.SUM>0) {
            Tb.setText(Double.toString(User.Balance));
        }

        if(User.Balance!=0) {
            Tf6.setDisable(false);
        }

        if(User.SUM == 0) {
            Tb.setText(Double.toString(User.Balance));
            LinearGradient paint = new LinearGradient(
                    0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(0.1922, 0.3216, 0.8392, 1.0)),
                    new Stop(1.0, new Color(0.1216, 1.0, 0.4863, 1.0)));
            for (int i = 0; i < 6; ++i) {
                    pieC[i].setStartAngle(z += y);
                    y = 60;
                    pieC[i].setFill(paint);
                    pieC[i].setType(ArcType.ROUND);
            }
        }
        else {
            Tb.setText(Double.toString(User.Balance));
            for (int i = 0; i < 11; ++i) {
                pieC[i].setStartAngle(z += y);
                y = (User.Expense_Cat[i] / User.SUM) * 360;
                pieC[i].setLength(y);
                pieC[i].setFill(color[i]);
                pieC[i].setType(ArcType.ROUND);
                if(User.Expense_Cat[i]>0){
                    setRect(i);
                }
            }
        }
        //Platform.runLater(() -> {
            for (int i =0; i < series.getData().size(); ++i) {
                series.getData().get(i).setYValue(User.storeY[i]);
            }
        //});
        if(User.noti_counter==0) {
            Noti_label.setVisible(false);
            Noti_label.setDisable(true);
        }
        else{
            Noti_label.setText(String.valueOf(User.noti_counter));
            Noti_label.setVisible(true);
            Noti_label.setDisable(false);
        }
        for (int i = 0; i < User.NF_data.size(); ++i) {
            AnchorPane anchorpane = new AnchorPane();
            anchorpane.setStyle(
                    "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                            "-fx-background-radius: 20px;"
            );
            AnchorPane.setTopAnchor(User.NF_data.get(i).messsage, 20.0);
            AnchorPane.setLeftAnchor(User.NF_data.get(i).messsage, 10.0);
            anchorpane.setPrefSize(120, 80);
            anchorpane.getChildren().add(User.NF_data.get(i).messsage);
            vbox.getChildren().add(1,anchorpane);
        }
    }

    public void OpenNoti_pane() {
        User.noti_counter=0;
        Noti_label.setVisible(false);
        Noti_label.setDisable(true);
        Noti_pane.setVisible(true);
        Noti_pane.setDisable(false);
    }

    public void CloseNoti_pane() {
       Noti_pane.setVisible(false);
       Noti_pane.setDisable(true);
    }

    public void Budget_Goal(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Goalsetter.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("pbcustom.css")).toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void Monthly_Budget(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Monthly_budget.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void Fixed_Deposit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fixed_Deposit.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void Debt_Loan(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Debt & Loan.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("pbcustom.css")).toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void BankAccount(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("BankAccount.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void User_profile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("User_profile.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    public void Log_out(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        SQLConnection.updateDashboard();
        SQLConnection.updateCount(User.Name,User.goals_count,User.debts_count,User.lents_count);
        User.reset();
        flag=true;
        FixedDeposit_Controller.flag=true;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    public void miniScene(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), miniAnchor);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        miniAnchor.setDisable(false);
        miniAnchor.setVisible(true);
    }

    public void close(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), miniAnchor);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
        miniAnchor.setDisable(true);
        miniAnchor.setVisible(false);
    }

}
