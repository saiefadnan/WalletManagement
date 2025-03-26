package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class PieChart_controller implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private TextField textField1,textField2,nameFld,noteFld,reachedFld;
    @FXML
    private Label myLabel,label2;
    @FXML
    private Button backBtn,changeDateBtn,changeNameBtn,changeNoteBtn,changeReachedBtn;
    @FXML
    private DatePicker changeDateFld;
    @FXML
    private Label goalLabel,dateLabel,noteLabel,label3;
    static double[] values=new double[3];
    ObservableList<PieChart.Data> pieChartData;
    private static String s="";
    private static int i;
    @Override
    public void initialize(URL url, ResourceBundle resourcebundle)
    {
        s=goals_tab.goals_name;
        i=User.ap_name.indexOf(s);
        goalLabel.setText(s);
        dateLabel.setText(User.ap_date.get(i).toString());
        noteLabel.setText(User.ap_note.get(i));
        double remaining=User.ap_target.get(i)-User.ap_saved.get(i);
        DecimalFormat df=new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        label2.setText(String.format("%.2f",remaining));
        textField1.setText(String.format("%.2f",User.ap_target.get(i)));
        label3.setText(String.format("%.2f",User.ap_saved.get(i))+" + ");
        if(remaining<=0)
        {
            myLabel.setText("Congratulations! You have reached your goal.");
            myLabel.setStyle("-fx-text-fill: Green;");
        }
        pieChartData= FXCollections.observableArrayList(
            new PieChart.Data("Goal Remain",remaining),
            new PieChart.Data("Goal Reached",User.ap_saved.get(i))
        );
        pieChart.getData().addAll(pieChartData);
    }
    @FXML
    private void back(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Goalsetter.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("pbcustom.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
        stage.setFullScreen(true);
    }
    public void getTarget() {
        try {
            values[0] = Double.parseDouble(textField1.getText());
            if (values[0] <= User.ap_saved.get(i)) {
                myLabel.setText("Congratulations! You have reached your goal.");
                myLabel.setStyle("-fx-text-fill: Green;");
                values[2] = 0;
                setValues();
                label2.setText(String.format("%.2f", values[2]));
                label3.setText(String.format("%.2f", values[0]) + " + ");
                //SQLConnection.updateGoals(s, values[0], values[0], s, User.ap_date.get(i), User.ap_note.get(i));
                Supabase.getInstance().updateGoals(s, values[0], values[0], s, User.ap_date.get(i), User.ap_note.get(i));
                User.ap_target.set(i, values[0]);
                User.ap_saved.set(i, values[0]);
                return;
            }
            values[2] = values[0] - User.ap_saved.get(i);
            setValues();
            label2.setText(String.format("%.2f", values[2]));
            label3.setText(String.format("%.2f", User.ap_saved.get(i)) + " + ");
            //SQLConnection.updateGoals(s, values[0], User.ap_saved.get(i), s, User.ap_date.get(i), User.ap_note.get(i));
            Supabase.getInstance().updateGoals(s, values[0], User.ap_saved.get(i), s, User.ap_date.get(i), User.ap_note.get(i));
            User.ap_target.set(i, values[0]);
            myLabel.setText("Target amount updated successfully.");
            myLabel.setStyle("-fx-text-fill: green;");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        } catch (NumberFormatException e) {
            myLabel.setText("Invalid input. Please enter the amount in numbers.");
            myLabel.setStyle("-fx-text-fill: red;");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        } catch (Exception e) {
            System.out.println("Error");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        }
    }
    public void getValues() {
        try {
            values[0] = User.ap_target.get(i);
            values[1] = User.ap_saved.get(i)+Double.parseDouble(textField2.getText());
            if(values[0]<=values[1]){
                myLabel.setText("Congratulations! You have reached your goal.");
                myLabel.setStyle("-fx-text-fill: Green;");
                values[2]=0;
                setValues();
                label2.setText(String.format("%.2f",values[2]));
                label3.setText(String.format("%.2f",values[0])+" + ");
                //SQLConnection.updateGoals(s,values[0],values[0],s,User.ap_date.get(i),User.ap_note.get(i));
                Supabase.getInstance().updateGoals(s,values[0],values[0],s,User.ap_date.get(i),User.ap_note.get(i));
                User.ap_target.set(i,values[0]);
                User.ap_saved.set(i,values[0]);
                return;
            }
            values[2]=values[0]-values[1];
            setValues();
            label2.setText(String.format("%.2f",values[2]));
            label3.setText(String.format("%.2f",values[1])+" + ");
            //SQLConnection.updateGoals(s,values[0],values[1],s,User.ap_date.get(i),User.ap_note.get(i));
            Supabase.getInstance().updateGoals(s,values[0],values[1],s,User.ap_date.get(i),User.ap_note.get(i));
            User.ap_target.set(i,values[0]);
            User.ap_saved.set(i,values[1]);
            myLabel.setText("Saved amount updated successfully.");
            myLabel.setStyle("-fx-text-fill: green;");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        }
        catch(NumberFormatException e)
        {
            myLabel.setText("Invalid input. Please enter the amount in numbers.");
            myLabel.setStyle("-fx-text-fill: red;");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        }
        catch(Exception e) {
            System.out.println("Error");
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> {
                fadeOutTransition.play();
            });
            pause.play();
        }
    }
    public void change_reached(){
        reachedFld.setDisable(false);
        reachedFld.setVisible(true);
        reachedFld.setText(String.format("%.2f",User.ap_saved.get(i)));
        changeReachedBtn.setDisable(true);
        changeReachedBtn.setVisible(false);
    }
    public void reachedChange() throws SQLException, ClassNotFoundException {
        double d=Double.parseDouble(reachedFld.getText());
        if(d>=User.ap_target.get(i)){
            myLabel.setText("Congratulations! You have reached your goal.");
            myLabel.setStyle("-fx-text-fill: green;");
            values[2]=0;
            values[1]=User.ap_target.get(i);
            setValues();
            label2.setText(String.format("%.2f",values[2]));
            label3.setText(String.format("%.2f",values[1])+" + ");
            //SQLConnection.updateGoals(s,values[1],values[1],s,User.ap_date.get(i),User.ap_note.get(i));
            Supabase.getInstance().updateGoals(s,values[1],values[1],s,User.ap_date.get(i),User.ap_note.get(i));
            User.ap_saved.set(i,values[1]);
            myLabel.setStyle("-fx-text-fill: green;");
            reachedFld.setDisable(true);
            reachedFld.setVisible(false);
            changeReachedBtn.setDisable(false);
            changeReachedBtn.setVisible(true);
            return;
        }
        values[1]=d;
        values[2]=User.ap_target.get(i)-values[1];
        setValues();
        label2.setText(String.format("%.2f",values[2]));
        label3.setText(String.format("%.2f",values[1])+" + ");
        //SQLConnection.updateGoals(s,User.ap_target.get(i),values[1],s,User.ap_date.get(i),User.ap_note.get(i));
        Supabase.getInstance().updateGoals(s,User.ap_target.get(i),values[1],s,User.ap_date.get(i),User.ap_note.get(i));
        User.ap_saved.set(i,values[1]);
        myLabel.setText("Saved amount updated successfully.");
        reachedFld.setDisable(true);
        reachedFld.setVisible(false);
        changeReachedBtn.setDisable(false);
        changeReachedBtn.setVisible(true);
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            fadeOutTransition.play();
        });
        pause.play();
    }
    public void change_date(){
        changeDateFld.setDisable(false);
        changeDateFld.setVisible(true);
        changeDateBtn.setDisable(true);
        changeDateBtn.setVisible(false);
    }
    public void datePick() throws SQLException, ClassNotFoundException {
        //String p=changeDateFld.getValue().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate p=changeDateFld.getValue();
        dateLabel.setText(p.toString());
        changeDateFld.setDisable(true);
        changeDateFld.setVisible(false);
        Supabase.getInstance().updateGoals(s,User.ap_target.get(i),User.ap_saved.get(i),s,p,User.ap_note.get(i));
        User.ap_date.set(i,p);
        changeDateBtn.setDisable(false);
        changeDateBtn.setVisible(true);
        myLabel.setText("Date updated successfully.");
        myLabel.setStyle("-fx-text-fill: green;");
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            fadeOutTransition.play();
        });
        pause.play();
    }
    public void change_name(){
        nameFld.setDisable(false);
        nameFld.setVisible(true);
        nameFld.setText(User.ap_name.get(i));
        changeNameBtn.setDisable(true);
        changeNameBtn.setVisible(false);
    }
    public void nameChange() throws SQLException, ClassNotFoundException {
        String p=nameFld.getText();
        goalLabel.setText(p);
        nameFld.setDisable(true);
        nameFld.setVisible(false);
        //SQLConnection.updateGoals(s,User.ap_target.get(i),User.ap_saved.get(i),p,User.ap_date.get(i),User.ap_note.get(i));
        Supabase.getInstance().updateGoals(s,User.ap_target.get(i),User.ap_saved.get(i),p,User.ap_date.get(i),User.ap_note.get(i));
        User.ap_name.set(i,p);
        changeNameBtn.setDisable(false);
        changeNameBtn.setVisible(true);
        myLabel.setText("Name updated successfully.");
        myLabel.setStyle("-fx-text-fill: green;");
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            fadeOutTransition.play();
        });
        pause.play();
    }
    public void change_note(){
        noteFld.setDisable(false);
        noteFld.setVisible(true);
        noteFld.setText(User.ap_note.get(i));
        changeNoteBtn.setDisable(true);
        changeNoteBtn.setVisible(false);
    }
    public void noteChange() throws SQLException, ClassNotFoundException {
        String p=noteFld.getText();
        noteLabel.setText(p);
        noteFld.setDisable(true);
        noteFld.setVisible(false);
        //SQLConnection.updateGoals(s,User.ap_target.get(i),User.ap_saved.get(i),s,User.ap_date.get(i),p);
        Supabase.getInstance().updateGoals(s,User.ap_target.get(i),User.ap_saved.get(i),s,User.ap_date.get(i),p);
        User.ap_note.set(i,p);
        changeNoteBtn.setDisable(false);
        changeNoteBtn.setVisible(true);
        myLabel.setText("Note updated successfully.");
        myLabel.setStyle("-fx-text-fill: green;");
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), myLabel);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            fadeOutTransition.play();
        });
        pause.play();
    }
    public void setValues(){
        pieChartData.get(0).setPieValue(values[2]);
        pieChartData.get(1).setPieValue(values[1]);
    }
    public void deleteGoal(ActionEvent event){
        try{
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Removing Goal");
            alert.setContentText("Do you really want to delete the goal "+s+"?");
            alert.setHeaderText("Warning!");
            if(alert.showAndWait().get()== ButtonType.OK) {
                //SQLConnection.deleteData(User.Name, s);
                User.goals_count--;
                System.out.println(User.goals_count);
                User.ap_name.remove(i);
                User.ap_target.remove(i);
                User.ap_saved.remove(i);
                User.ap_date.remove(i);
                User.ap_note.remove(i);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Goalsetter.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("pbcustom.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
                stage.setFullScreen(true);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
