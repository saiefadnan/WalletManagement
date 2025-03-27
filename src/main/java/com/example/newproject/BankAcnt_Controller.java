package com.example.newproject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class BankAcnt_Controller extends Abstract_controller {

    @FXML
    private ChoiceBox choicebox,choicebox1;
    @FXML
    private Label label;
    @FXML
    private Label Brif_msg;
    @FXML
    private PasswordField cust_no;
    @FXML
    private PasswordField Acc_no;
    @FXML
    private TextField user_field;
    @FXML
    private Label datetime;
    @FXML
    private Label acc_id;
    @FXML
    private Label des;
    @FXML
    private Label amount;
    @FXML
    private Label curr;
    @FXML
    private Label cd_indctr;
    @FXML
    private Label name;
    @FXML
    private Label acc_type;
    @FXML
    private Label acc_subtype;
    private static final String clientId = "v_miNUvXtCd81d92DHlqkfFSvCnmCZhU1TnWRT8JEX4=";
    private static final String clientSecret = "oBAq02jlepwF1PqRLQmodHOGG5N2VMhnJW0Uww638tM=";
    private static final String tokenEndpoint = "https://ob.sandbox.natwestinternational.com/token";
    private static final String CONSENT_ENDPOINT = "https://ob.sandbox.natwestinternational.com/open-banking/v3.1/aisp/account-access-consents";
    private static final String AUTHORIZE_ENDPOINT = "https://api.sandbox.natwestinternational.com/authorize";
    private static final String redirect_uri = "https://webbanking.sandbox.natwestinternational.com/?domainName=b51cd244-6d74-4ef0-b5b7-46f271272a25.example.org/callback";
    private static final String API_ENDPOINT = "https://ob.sandbox.natwestinternational.com/open-banking/v3.1/aisp/accounts";
    private static String choice,choice1,user,Customer_Number,Account_Number;
    private static String accessToken;
    private static String consentId;
    private static String auth_code;
    private static String final_accessToken;
    private static String Account_id;
    private static Boolean permission;
    private static String accountType;
    private static String accountSubType;
    private static String description;
    private static String nickname;
    private static String creditDebitIndicator;
    private static String type;
    private static String dateTime;
    private static String amountValue;
    private static String currency;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choicebox.getItems().add("International Natwest Bank");
        choicebox1.getItems().add("Bangladesh");
        Init();
    }
    @Override
    public void Init() {
        Customer_Number = null;
        Account_Number = null;
        choice = null;
        choice1 = null;
        user = null;
        permission=false;
        name.setText("Name                           :             ");
        acc_type.setText("Account Type               :            ");
        acc_subtype.setText("Account Subtype          :           ");
        des.setText("Description                    :             ");
        amount.setText("Amount                         :             ");
        curr.setText("Currency                        :             ");
        cd_indctr.setText("Credit/Debit Indicator   :             ");
        datetime.setText("Date-time : ");
        acc_id.setText("Account ID : ");
        label.setText("");
    }

    public void Call() {
        Init();
        Customer_Number=cust_no.getText();
        Account_Number=Acc_no.getText();
        choice= (String) choicebox.getValue();
        choice1= (String) choicebox1.getValue();

         user= user_field.getText();
        if(Objects.equals(Customer_Number, "") || Objects.equals(Account_Number, "") || Objects.equals(choice, "") || Objects.equals(choice1, "") || Objects.equals(user, "")) {
            Brif_msg.setText("Fill the empty fields!!!");
            ShowLabel();
        }
        else {
            try {
                Get_accessToken();
                makeConsentRequest();
                authorizeConsent1();
                exchangeCodewithToken();
                getAccountInformation();
                retrieveBalance();
                if(nickname.equals(user)) {
                    ShowScreen();
                    Brif_msg.setText("Successful!!!");
                    ShowLabel();
                    permission = true;
                    return;
                }
                label.setText("Failure!!!");
                Brif_msg.setText("Invalid username!!!");
                ShowLabel();
            } catch (IOException | InterruptedException e) {
                Brif_msg.setText("No Account Found!!!");
                ShowLabel();
            }
        }
    }

    public void webBrowse(){
        Customer_Number=cust_no.getText();
        Account_Number=Acc_no.getText();
        choice= (String) choicebox.getValue();
        choice1= (String) choicebox1.getValue();
        user= user_field.getText();
        if(Objects.equals(Customer_Number, "") || Objects.equals(Account_Number, "") || Objects.equals(choice, "") || Objects.equals(choice1, "") || Objects.equals(user, "")) {
            Brif_msg.setText("Fill the empty fields!!!");
            ShowLabel();
        }
        else {
            try {
                String authorizeUrl = buildAuthorizeUrl();
                if (!permission) {
                    Brif_msg.setText("An error occurred!!!");
                    ShowLabel();
                } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(authorizeUrl));
                }
            } catch (Exception e) {
                Brif_msg.setText("An error occurred!!!");
                ShowLabel();
                e.printStackTrace();
            }
        }
    }

    private String buildAuthorizeUrl() throws UnsupportedEncodingException {
        // Encode the parameters to handle spaces and other special characters
        String encodedScope = URLEncoder.encode("openid+accounts", StandardCharsets.UTF_8);
        String encodedRedirectUri = URLEncoder.encode("https://webbanking.sandbox.natwestinternational.com/?domainName=b51cd244-6d74-4ef0-b5b7-46f271272a25.example.org/callback", StandardCharsets.UTF_8.toString());
        String encodedResponseType = URLEncoder.encode("code id_token", StandardCharsets.UTF_8);
        // Build the authorization URL
        return AUTHORIZE_ENDPOINT +
                "?client_id=" + clientId +
                "&response_type=" + encodedResponseType +
                "&scope=" + encodedScope +
                "&redirect_uri=" + encodedRedirectUri +
                "&request=" + URLEncoder.encode(consentId, StandardCharsets.UTF_8);
    }

    public void Get_accessToken() throws IOException {
        URL url = new URL(tokenEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String postData = "grant_type=client_credentials&client_id=" + clientId +
                "&client_secret=" + clientSecret + "&scope=accounts";
        try (var os = connection.getOutputStream()) {
            os.write(postData.getBytes(StandardCharsets.UTF_8));
        }
        try (var br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String response = br.readLine();
            accessToken = extractAccessToken(response);
        }
    }

    private void makeConsentRequest() throws IOException {
        URL url = new URL(CONSENT_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        String postData = "{\n" +
                "  \"Data\": {\n" +
                "    \"Permissions\": [\n" +
                "      \"ReadAccountsDetail\",\n" +
                "      \"ReadBalances\",\n" +
                "      \"ReadTransactionsCredits\",\n" +
                "      \"ReadTransactionsDebits\",\n" +
                "      \"ReadTransactionsDetail\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"Risk\": {}\n" +
                "}";

        try (var os = connection.getOutputStream()) {
            os.write(postData.getBytes(StandardCharsets.UTF_8));
        }
        try (var br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String response = br.readLine();
            processConsentResponse(response);
        }
    }

    private void authorizeConsent1() throws IOException {
        String authorizeUrl = buildAuthorizeUrl1();
        System.out.println("Authorization URL: " + authorizeUrl);
        URL url = new URL(authorizeUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        try
                (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            auth_code = parseAuthCodeFromResponse(response.toString());
        }
    }

    private String buildAuthorizeUrl1() {
        String encodedScopeOpenID = URLEncoder.encode("openid", StandardCharsets.UTF_8);
        String encodedScopeAccounts = URLEncoder.encode("accounts", StandardCharsets.UTF_8);
        String encodedScope = encodedScopeOpenID + "+" + encodedScopeAccounts;
        String authorizationAccounts = URLEncoder.encode(Account_Number, StandardCharsets.UTF_8);
        String encodedRedirectUri = URLEncoder.encode(redirect_uri, StandardCharsets.UTF_8);
        String encodedResponseType = URLEncoder.encode("code id_token", StandardCharsets.UTF_8);
        // Add programmatic authorization parameters
        String authorizationMode = URLEncoder.encode("AUTO_POSTMAN", StandardCharsets.UTF_8);
        String authorizationResult = URLEncoder.encode("APPROVED", StandardCharsets.UTF_8);
        String authorizationUsername = URLEncoder.encode(Customer_Number+"@b51cd244-6d74-4ef0-b5b7-46f271272a25.example.org", StandardCharsets.UTF_8);
        // Build the authorization URL with programmatic authorization parameters
        return AUTHORIZE_ENDPOINT +
                "?client_id=" + clientId +
                "&response_type=" + encodedResponseType +
                "&scope=" + encodedScope +
                "&redirect_uri=" + encodedRedirectUri +
                "&request=" + URLEncoder.encode(consentId, StandardCharsets.UTF_8) +
                "&authorization_mode=" + authorizationMode +
                "&authorization_result=" + authorizationResult +
                "&authorization_username=" + authorizationUsername +
                "&authorization_accounts=" + authorizationAccounts;
    }

    private void exchangeCodewithToken() throws IOException, InterruptedException {
        String encodedRedirectUri = URLEncoder.encode(redirect_uri, StandardCharsets.UTF_8);
        String requestBody = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&redirect_uri=" + encodedRedirectUri +
                "&grant_type=authorization_code" +
                "&code=" + URLEncoder.encode(auth_code, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Response Code: " + response.statusCode());
        final_accessToken=extractAccessToken(response.body());
    }

    private void getAccountInformation() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Authorization", "Bearer " + final_accessToken)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Response Code: " + response.statusCode());
        JSONObject jsonObject=new JSONObject(response.body());
        JSONArray accounts = jsonObject.getJSONObject("Data").getJSONArray("Account");
        //label.setText("Account info is fetched sucessfully!!!\n\n");
        for (int i = 0; i < accounts.length(); i++) {
            JSONObject account = accounts.getJSONObject(i);
            Account_id = account.getString("AccountId");
            accountType = account.getString("AccountType");
            accountSubType = account.getString("AccountSubType");
            description = account.getString("Description");
            nickname = account.getString("Nickname");
        }
    }

    public void retrieveBalance(){
        String balanceEndpoint = "https://api.sandbox.natwestinternational.com/open-banking/v3.1/aisp/accounts/{accountId}/balances";
        try {
            URL balanceUrl = new URL(balanceEndpoint.replace("{accountId}", Account_id));
            HttpURLConnection connection = (HttpURLConnection) balanceUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-fapi-financial-id", "0015800001ZEZ1lAAH");
            connection.setRequestProperty("x-fapi-auth-date", "Sun, 10 Sep 2017 19:43:31 UTC");
            connection.setRequestProperty("x-fapi-customer-ip-address", "192.168.1.100");
            connection.setRequestProperty("x-fapi-interaction-id", "rfc4122_uid");
            connection.setRequestProperty("Authorization", "Bearer " + final_accessToken);
            // Read the response
            try (BufferedReader balanceReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder balanceResponse = new StringBuilder();
                String balanceLine;

                while ((balanceLine = balanceReader.readLine()) != null) {
                    balanceResponse.append(balanceLine);
                }
                // Parse and handle the balance response as needed
                JSONObject jsonResponse=new JSONObject(balanceResponse.toString());
                if (jsonResponse.has("Data")) {
                    JSONObject data = jsonResponse.getJSONObject("Data");
                    if (data.has("Balance")) {
                        JSONArray balances = data.getJSONArray("Balance");
                        for (int i = 0; i < balances.length(); i++) {
                            JSONObject balance = balances.getJSONObject(i);
                            creditDebitIndicator = balance.getString("CreditDebitIndicator");
                            type = balance.getString("Type");
                            dateTime = balance.getString("DateTime");
                            JSONObject amount = balance.getJSONObject("Amount");
                            amountValue = amount.getString("Amount");
                            currency = amount.getString("Currency");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowScreen(){
        name.setText("Name                           :             "+nickname);
        acc_type.setText("Account Type               :            "+accountType);
        acc_subtype.setText("Account Subtype          :           "+accountSubType);
        des.setText("Description                    :             "+description);
        amount.setText("Amount                         :             "+amountValue);
        curr.setText("Currency                        :             "+currency);
        cd_indctr.setText("Credit/Debit Indicator   :             "+creditDebitIndicator);
        datetime.setText("Date-time : "+dateTime);
        acc_id.setText("Account ID : "+Account_id);
    }

    private static String extractAccessToken(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("access_token");
    }

    private void processConsentResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        consentId = jsonResponse.getJSONObject("Data").getString("ConsentId");
        System.out.println("Consent ID: " + consentId);
    }

    private static String parseAuthCodeFromResponse(String response) {
        int codeIndex = response.indexOf("code=");
        if (codeIndex != -1) {
            int ampersandIndex = response.indexOf("&", codeIndex);
            return response.substring(codeIndex + 5, ampersandIndex != -1 ? ampersandIndex : response.length());
        }
        return null;
    }
    public void ShowLabel(){
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

    public void Back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashBoard.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,splashWindow.width,splashWindow.height);
        stage.setScene(scene);
        stage.show();
        //stage.setFullScreen(true);
    }
}

//Domain_Name:->b51cd244-6d74-4ef0-b5b7-46f271272a25.example.org
//123456789012
//XXXX50000012345601