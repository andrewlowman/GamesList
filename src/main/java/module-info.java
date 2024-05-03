module com.example.gameslist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.lowman.gameslist to javafx.fxml;
    opens com.lowman.gameslist.Controller to javafx.fxml;
    opens com.lowman.gameslist.Model to javafx.base;
    exports com.lowman.gameslist;
}