package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Utility.HLTB;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoadPSNController implements Initializable {
    public Button loadPSNCancelButton;
    public Button loadPSNOKButton;
    public Label loadPSNLabel;
    public Button loadPSNDoneButton;
    private WebDriver driver;
    private HLTB hltb;

    WebDriverWait wait;
    HashMap<String, String> titlesMap = new HashMap<>();

    public LoadPSNController() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hltb = new HLTB();
        loadPSNLabel.setText("Use the first Firefox window. Log into Playstation account and navigate to games library");

        loadPSNCancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                close();
            }
        });

        loadPSNOKButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class= 'psw-l-anchor psw-t-body psw-c-t-1 psw-t-truncate-2 psw-m-b-3']")));
                }catch(Exception e){
                    dialogPopUp("Something went wrong. Maybe the psn library didn't load");
                }

                WebElement nextButton = driver.findElement(By.xpath("//button[@aria-value='Go to next page']"));
                while(nextButton.isDisplayed()){
                    nextButton.click();
                }
            }
        });

        loadPSNDoneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

    }

    public void close(){
        Stage stage = (Stage) loadPSNCancelButton.getScene().getWindow();
        stage.close();
    }

    public void dialogPopUp(String text){
        Dialog<String> d = new Dialog<String>();
        d.setContentText(text);
        d.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        d.showAndWait();
    }
}
