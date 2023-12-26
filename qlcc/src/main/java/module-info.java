module com.example.qlcc {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires mysql.connector.java;

    requires com.almasb.fxgl.all;
    requires java.sql;


    opens com.example.qlcc to javafx.fxml;
    exports com.example.qlcc;
    exports com.example.qlcc.Controller;
    opens com.example.qlcc.Controller to javafx.fxml;
    exports com.example.qlcc.DataModel;
    opens com.example.qlcc.DataModel to javafx.fxml;
}