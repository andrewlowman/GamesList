module com.example.gameslist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.seleniumhq.selenium.support;


    opens com.lowman.gameslist to javafx.fxml;
    opens com.lowman.gameslist.Controller to javafx.fxml;
    opens com.lowman.gameslist.Model to javafx.base;
    exports com.lowman.gameslist;
}