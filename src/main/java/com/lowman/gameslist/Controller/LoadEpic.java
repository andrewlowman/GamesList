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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;


//Epic uses cloudflare and I can't bypass it.

public class LoadEpic implements Initializable {
    public Label loadEpicLabel;
    public Button loadEpicCancelButton;
    public Button loadEpicOKButton;
    public Button loadEpicDoneButton;
    private WebDriver driver = null;
    private HLTB hltb;

    public LoadEpic() {
       /* System.setProperty("webdriver.chrome.driver","C:\\Chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();*/
        File file = new File("C:/Users/low85/AppData/Roaming/Mozilla/Firefox/Profiles/5k2kz0ml.default-release");
        FirefoxProfile profile = new FirefoxProfile(file);
        //profile.setPreference("profile","C:/Users/low85/AppData/Roaming/Mozilla/Firefox/Profiles/5k2kz0ml.default-release");
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hltb = new HLTB();
        loadEpicLabel.setText("Use the first Firefox window opened. Navigate to the Epic store. Login to your account, click on account, click on transactions. Click OK below");

        loadEpicCancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                driver.close();
                Stage stage = (Stage) loadEpicCancelButton.getScene().getWindow();
                stage.close();
            }
        });

        loadEpicOKButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<WebElement> titles = List.of();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                try{
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payment-history-show-more-button")));
                }catch (Exception e){
                    dialogPopUp("Couldn't find show more button");
                }

                WebElement showMoreButton = driver.findElement(By.id("payment-history-show-more-button"));
                while(showMoreButton.isDisplayed()){
                    showMoreButton.click();
                }
            }
        });
    }

    public void dialogPopUp(String text){
        Dialog<String> d = new Dialog<String>();
        d.setContentText(text);
        d.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        d.showAndWait();
    }
}
