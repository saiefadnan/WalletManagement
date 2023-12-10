package com.example.newproject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class User_profile extends Abstract_controller{
    @FXML
    private VBox dvbox;
    @FXML
    private VBox mvbox;
    @FXML
    private VBox yvbox;
    @FXML
    private Label userLabel,nameLabel,genderLabel,dobLabel,professionLabel,addressLabel,phoneLabel,emailLabel;
    @FXML
    private Button changeBtn;
    @FXML
    private TextField nameTf,proTf,addTf,phoneTf,emailTf;
    @FXML
    private DatePicker dateTf;
    @FXML
    private RadioButton maleRd,femaleRd,othersRd;
    @FXML
    private Button saveBtn;
    @FXML
    private Label warnLabel;
    private final Color[] color = {Color.RED, Color.BLUE, Color.ORANGE, Color.PURPLE,Color.GREEN, Color.YELLOW,Color.DEEPPINK,Color.YELLOWGREEN,Color.VIOLET,Color.TURQUOISE,Color.GRAY};
    private final String[] choice={"Foods & Drinks","Shopping","Housing","Transportation","Vehicle","Life & Entertainment","Investments","Income","Communication","Financial expenses","Others"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Init();
    }

    @Override
    public void Init() {
        Daily_Rec();
        Monthly_Rec();
        Yearly_Rec();
        userLabel.setText(User.Name);
        setValue();
    }

    public void enableInfo(ActionEvent event){
        changeBtn.setDisable(true);changeBtn.setVisible(false);
        saveBtn.setDisable(false);saveBtn.setVisible(true);
        nameTf.setDisable(false);nameTf.setVisible(true);
        nameTf.setText(User.userInfo.get(0)+" "+User.userInfo.get(1));
        proTf.setDisable(false);proTf.setVisible(true);
        proTf.setText(User.userInfo.get(5));
        addTf.setDisable(false);addTf.setVisible(true);
        addTf.setText(User.userInfo.get(6));
        phoneTf.setDisable(false);phoneTf.setVisible(true);
        phoneTf.setText(User.userInfo.get(7));
        emailTf.setDisable(false);emailTf.setVisible(true);
        emailTf.setText(User.userInfo.get(2));
        dateTf.setDisable(false);dateTf.setVisible(true);
        dateTf.setValue(LocalDate.parse(User.userInfo.get(4), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        maleRd.setDisable(false);maleRd.setVisible(true);
        femaleRd.setDisable(false);femaleRd.setVisible(true);
        othersRd.setDisable(false);othersRd.setVisible(true);
        genderLabel.setVisible(false);String s=User.userInfo.get(3);
        if(Objects.equals(s, maleRd.getText()))
            maleRd.setSelected(true);
        else if(Objects.equals(s, femaleRd.getText()))
            femaleRd.setSelected(true);
        else
            othersRd.setSelected(true);
    }

    public void updateUserInfo() throws SQLException, ClassNotFoundException {
        String name=nameTf.getText();
        String[] arr=new String[2];
        if(name.contains(" "))
            arr=name.split(" ",2);
        else{
            arr[0]=name;
            arr[1]="";
        }
        String pro=proTf.getText();
        String add=addTf.getText();
        String phone=phoneTf.getText();
        String email=emailTf.getText();
        LocalDate date=dateTf.getValue();
        String dob= date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if(name.isEmpty()||pro.isEmpty()||add.isEmpty()||phone.isEmpty()||email.isEmpty()||dob.isEmpty()){
            warnLabel.setText("Please fill up all the fields");
            warnLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if(!phone.matches("[0-9]+")||phone.length()!=11){
            warnLabel.setText("Please enter a valid phone number");
            warnLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if(!email.matches("^(.+)@(.+)$")){
            warnLabel.setText("Please enter a valid email address");
            warnLabel.setStyle("-fx-text-fill: red");
            return;
        }
        User.userInfo.set(0,arr[0]);
        User.userInfo.set(1,arr[1]);
        User.userInfo.set(2,email);
        User.userInfo.set(3,maleRd.isSelected()?maleRd.getText():femaleRd.isSelected()?femaleRd.getText():othersRd.getText());
        User.userInfo.set(4,dob);
        User.userInfo.set(5,pro);
        User.userInfo.set(6,add);
        User.userInfo.set(7,phone);
        changeComplete();
        setValue();
        SQLConnection.updateInfo();
        warnLabel.setText("Information updated successfully");
        warnLabel.setStyle("-fx-text-fill: green");
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), warnLabel);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            fadeOutTransition.play();
        });
        pause.play();
    }
    public void changeComplete(){
        changeBtn.setDisable(false);changeBtn.setVisible(true);
        saveBtn.setDisable(true);saveBtn.setVisible(false);
        nameTf.setDisable(true);nameTf.setVisible(false);
        proTf.setDisable(true);proTf.setVisible(false);
        addTf.setDisable(true);addTf.setVisible(false);
        phoneTf.setDisable(true);phoneTf.setVisible(false);
        emailTf.setDisable(true);emailTf.setVisible(false);
        dateTf.setDisable(true);dateTf.setVisible(false);
        maleRd.setDisable(true);maleRd.setVisible(false);
        femaleRd.setDisable(true);femaleRd.setVisible(false);
        othersRd.setDisable(true);othersRd.setVisible(false);
        genderLabel.setVisible(true);
    }
    public void setValue(){
        nameLabel.setText(User.userInfo.get(0)+" "+User.userInfo.get(1));
        genderLabel.setText(User.userInfo.get(3));
        dobLabel.setText(User.userInfo.get(4));
        professionLabel.setText(User.userInfo.get(5));
        addressLabel.setText(User.userInfo.get(6));
        phoneLabel.setText(User.userInfo.get(7));
        emailLabel.setText(User.userInfo.get(2));
    }

    public void Daily_Rec(){
        for (int i = 0; i < 11; ++i) {
            Date_CategoryKey key = new Date_CategoryKey(LocalDate.now(), choice[i]);
            if (User.Expense_data.containsKey(key) && User.Expense_data.get(key) != 0) {
                AnchorPane pane = new AnchorPane();
                pane.setPrefSize(730, 81);
                pane.setStyle(
                        "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                                "-fx-background-radius: 20px;"
                );

                Text text0 = new Text();
                Text text2 = new Text();
                if(choice[i].equals("Income")){
                    text0.setText("+BDT  " + User.Expense_data.get(key));
                    text2.setText(choice[i]);
                    text0.setFill(Color.GREEN);
                }
                else{
                    text0.setText("-BDT  " + User.Expense_data.get(key));
                    text2.setText(choice[i]);
                    text0.setFill(Color.RED);
                }

                Text text1 = new Text(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyy")));
                text0.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));

                Rectangle rec = new Rectangle();
                rec.setHeight(12);
                rec.setWidth(12);
                rec.setArcWidth(8);
                rec.setArcHeight(8);
                rec.setStroke(Color.BLACK);
                rec.setStrokeWidth(1);
                rec.setFill(color[i]);

                AnchorPane.setLeftAnchor(text2, 80.0);
                AnchorPane.setTopAnchor(text2, 50.0);
                AnchorPane.setLeftAnchor(text1, 30.0);
                AnchorPane.setTopAnchor(text1, 10.0);
                AnchorPane.setRightAnchor(text0, 80.0);
                AnchorPane.setTopAnchor(text0, 50.0);
                AnchorPane.setTopAnchor(rec, 50.0);
                AnchorPane.setLeftAnchor(rec, 30.0);

                pane.getChildren().addAll(text0, text1, text2, rec);
                dvbox.getChildren().add(0, pane);
            }
        }
    }

    public void Monthly_Rec(){
        for (int i = 0; i < 11; ++i) {
            for (int j = 29; j >= 0; j-- ){
                LocalDate date = LocalDate.now().minusDays(j);
                Date_CategoryKey key = new Date_CategoryKey(date, choice[i]);
                if (User.Expense_data.containsKey(key) && User.Expense_data.get(key) != 0) {
                    AnchorPane pane = new AnchorPane();
                    pane.setPrefSize(730, 81);
                    pane.setStyle(
                            "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                                    "-fx-background-radius: 20px;"
                    );

                    Text text0 = new Text();
                    Text text2 = new Text();
                    if(choice[i].equals("Income")){
                        text0.setText("+BDT  " + User.Expense_data.get(key));
                        text2.setText(choice[i]);
                        text0.setFill(Color.GREEN);
                    }
                    else{
                        text0.setText("-BDT  " + User.Expense_data.get(key));
                        text2.setText(choice[i]);
                        text0.setFill(Color.RED);
                    }

                    Text text1 = new Text(date.format(DateTimeFormatter.ofPattern("dd MMM yyy")));
                    text0.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));

                    Rectangle rec = new Rectangle();
                    rec.setHeight(12);
                    rec.setWidth(12);
                    rec.setArcWidth(8);
                    rec.setArcHeight(8);
                    rec.setStroke(Color.BLACK);
                    rec.setStrokeWidth(1);
                    rec.setFill(color[i]);

                    AnchorPane.setLeftAnchor(text2, 80.0);
                    AnchorPane.setTopAnchor(text2, 50.0);
                    AnchorPane.setLeftAnchor(text1, 30.0);
                    AnchorPane.setTopAnchor(text1, 10.0);
                    AnchorPane.setRightAnchor(text0, 80.0);
                    AnchorPane.setTopAnchor(text0, 50.0);
                    AnchorPane.setTopAnchor(rec, 50.0);
                    AnchorPane.setLeftAnchor(rec, 30.0);

                    pane.getChildren().addAll(text0, text1, text2, rec);
                    mvbox.getChildren().add(0, pane);
                }
            }
        }
    }

    public void Yearly_Rec(){
        for (int i = 0; i < 11; ++i) {
            for (int j = 364; j >= 0; j-- ){
                LocalDate date = LocalDate.now().minusDays(j);
                Date_CategoryKey key = new Date_CategoryKey(date, choice[i]);
                if (User.Expense_data.containsKey(key) && User.Expense_data.get(key) != 0) {
                    AnchorPane pane = new AnchorPane();
                    pane.setPrefSize(730, 81);
                    pane.setStyle(
                            "-fx-background-color: linear-gradient(from 0.0% 0.0% to 100.0% 100.0%, #3152d6 0.0%, #1fff7ce0 100.0%);" +
                                    "-fx-background-radius: 20px;"
                    );

                    Text text0 = new Text();
                    Text text2 = new Text();
                    if(choice[i].equals("Income")){
                        text0.setText("+BDT  " + User.Expense_data.get(key));
                        text2.setText(choice[i]);
                        text0.setFill(Color.GREEN);
                    }
                    else{
                        text0.setText("-BDT  " + User.Expense_data.get(key));
                        text2.setText(choice[i]);
                        text0.setFill(Color.RED);
                    }
                    Text text1 = new Text(date.format(DateTimeFormatter.ofPattern("dd MMM yyy")));
                    text0.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));

                    Rectangle rec = new Rectangle();
                    rec.setHeight(12);
                    rec.setWidth(12);
                    rec.setArcWidth(8);
                    rec.setArcHeight(8);
                    rec.setStroke(Color.BLACK);
                    rec.setStrokeWidth(1);
                    rec.setFill(color[i]);

                    AnchorPane.setLeftAnchor(text2, 80.0);
                    AnchorPane.setTopAnchor(text2, 50.0);
                    AnchorPane.setLeftAnchor(text1, 30.0);
                    AnchorPane.setTopAnchor(text1, 10.0);
                    AnchorPane.setRightAnchor(text0, 80.0);
                    AnchorPane.setTopAnchor(text0, 50.0);
                    AnchorPane.setTopAnchor(rec, 50.0);
                    AnchorPane.setLeftAnchor(rec, 30.0);

                    pane.getChildren().addAll(text0, text1, text2, rec);
                    yvbox.getChildren().add(0, pane);
                }
            }
        }
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
