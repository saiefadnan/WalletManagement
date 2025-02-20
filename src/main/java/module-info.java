module com.example.newproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javax.mail;
    requires org.json;
    requires java.net.http;
    requires java.desktop;
    requires jakarta.mail.api;
    requires mysql.connector.j;
    requires org.postgresql.jdbc;
    requires dotenv.java;

    opens com.example.newproject to javafx.fxml;
    exports com.example.newproject;
}