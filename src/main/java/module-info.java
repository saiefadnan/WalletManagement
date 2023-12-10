module com.example.newproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.mail;
    requires org.json;
    requires java.net.http;
    requires java.desktop;
    requires mysql.connector.j;

    opens com.example.newproject to javafx.fxml;
    exports com.example.newproject;
}