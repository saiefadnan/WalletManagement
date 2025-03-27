module com.example.newproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    //requires javax.mail;
    requires java.prefs;
    requires org.json;
    requires java.net.http;
    requires java.desktop;
    requires mysql.connector.java;
    requires org.postgresql.jdbc;
    requires tyrus.server;
    requires tyrus.container.grizzly.server;
    //requires javax.websocket.api;
    //requires tyrus.core;
    requires tyrus.standalone.client;
    requires jakarta.activation;
    requires jakarta.mail;
    requires java.dotenv;

    opens com.example.newproject to javafx.base, javafx.controls, javafx.fxml;
    exports com.example.newproject;
}