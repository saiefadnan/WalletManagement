package com.example.newproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BudgetOView_Controller extends Abstract_controller {
    @FXML
    private ProgressBar Pgb;
    @FXML
    private Arc Expense_Arc;
    @FXML
    private LineChart linechart;
    @FXML
    private Text txt,txt1,txt2,txt3,txt4,Ts;
    @FXML
    private Rectangle rect;
    private XYChart.Series<String, Number> series;
    public static BudgetPlan mb_data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Init();
    }

    @Override
    public void Init(){
        ReturnUI();
        customizeChart();
        schedule_();
    }

    public void ReturnUI(){
        Pgb.setProgress(mb_data.progress);
        if(mb_data.progress>0.8)
            Pgb.setStyle("-fx-accent: red;");
        else {
            Pgb.setStyle("-fx-accent: green;");
        }

        Ts.setText(String.format("%.2f", mb_data.progress*100)+"%");
        Expense_Arc.setStroke(mb_data.cat_color);
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(Expense_Arc.lengthProperty(), mb_data.progress * 360))
            );
            timeline.play();
        });

        rect.setFill(mb_data.cat_color);

        txt.setText(mb_data.selected_cat);
        txt1.setText("BDT "+mb_data.limit_amount);
        txt2.setText("BDT "+mb_data.expense_amount+"\nSpent");

        double amount=mb_data.limit_amount-mb_data.expense_amount;
        if(mb_data.limit_amount>=mb_data.expense_amount){
            txt3.setText("BDT "+amount+"\nRemains");
        }
        else {
            txt3.setText("BDT "+Math.abs(amount)+"\nOverspent");
        }
        txt4.setText(mb_data.init_date.format(DateTimeFormatter.ofPattern("dd MMM yyy"))+" - "+mb_data.final_date.format(DateTimeFormatter.ofPattern("dd MMM yyy")));

        series = new XYChart.Series<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM");

        //mb_data.Ypoints[0]=mb_data.expense_amount;
        LocalDate today = LocalDate.now();
        for (int i =29; i >=0; i--) {
            LocalDate before_iDay_date = today.minusDays(i);
            Date_CategoryKey key = new Date_CategoryKey(before_iDay_date ,mb_data.selected_cat);
            if(!User.Expense_data.containsKey(key)){
                User.Expense_data.put(key,0.0);
            }
            Double expense = User.Expense_data.get(key);
            if (i == 0) {
                series.getData().add(new XYChart.Data<>("Today",expense));
            }
            else {
                series.getData().add(new XYChart.Data<>(LocalDate.now().minusDays(i).format(dateFormatter), expense));
            }
        }
        linechart.getData().add(series);
    }
    public void schedule_() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::checkUpdateChart, 0, 5, TimeUnit.SECONDS);
    }
    public void checkUpdateChart(){
        if(!mb_data.Today1.equals(LocalDate.now())){
            UpdateChart();
            mb_data.Today1=LocalDate.now();
        }
    }
    public void UpdateChart(){
        double[] y = new double[30];
        for (int i = 1; i < series.getData().size(); ++i) {
            XYChart.Data<String, Number> nextDataPoint = series.getData().get(i);
            y[i] = nextDataPoint.getYValue().doubleValue();
            mb_data.Ypoints[i-1]=y[i];
        }
        Date_CategoryKey key = new Date_CategoryKey(LocalDate.now(), mb_data.selected_cat);
        if(!User.Expense_data.containsKey(key)){
            User.Expense_data.put(key,0.0);
        }
        mb_data.Ypoints[series.getData().size()-1]= User.Expense_data.get(key);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM");
        Platform.runLater(() -> {
            series.getData().clear();
            for (int i=0;i<30;++i) {
                if (i == 29)
                    series.getData().add(new XYChart.Data<>("Today", mb_data.Ypoints[i]));
                else
                    series.getData().add(new XYChart.Data<>(LocalDate.now().minusDays(i).format(dateFormatter),mb_data.Ypoints[i]));
            }
        });
    }
    public void customizeChart(){
        series.setName("Expense Trend");
        series.getNode().setStyle("-fx-stroke: green;");
        String backgroundColor = "linear-gradient(to bottom, #3152d6 0.0%, #ff2121e0 100.0%)";
        String plotBackgroundStyle = "-fx-background-color: " + backgroundColor + ";";
        linechart.lookup(".chart-plot-background").setStyle(plotBackgroundStyle);
    }
    public void Back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Monthly_Budget.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
}
