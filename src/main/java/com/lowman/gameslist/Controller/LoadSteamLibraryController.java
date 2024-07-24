package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Utility.DBConnection;
import com.lowman.gameslist.Utility.HLTB;
import com.lowman.gameslist.Utility.Load;
import com.lowman.gameslist.Utility.Save;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LoadSteamLibraryController implements Initializable {
    public Label loadSteamLabel;
    public GridPane loadSteamPanel;
    public Button loadSteamOKButton;
    public Button loadSteamCancelButton;
    public Button loadSteamDoneButton;

    private WebDriver driver;
    private HashMap<String,Boolean> steamTitles = new HashMap<>();
    private HLTB hltb;
    Connection conn = null;

    public LoadSteamLibraryController(){
        driver = new FirefoxDriver();
        driver.manage().window().maximize();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hltb = new HLTB();
        Load load = new Load();
        Save save = new Save();
        loadSteamLabel.setText("Use the first Firefox window opened. Login to Steam. Click on your profile. Click on Games on the right side of the screen. Click OK below");

        loadSteamOKButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<WebElement> titles = List.of();
                WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(20)).withMessage("Something went wrong");

                try{
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div//span//a")));
                }catch(Exception e){
                    dialogPopUp("Couldn't load titles");
                }

                titles = driver.findElements(By.xpath("//div[@tabindex='0']//span//a"));
                for(WebElement w:titles){
                    String titleScraped = w.getText().replaceAll("[^\\d_\\p{IsAlphabetic}_\\s]", "");
                    steamTitles.put(titleScraped,false);
                }

                if(!steamTitles.isEmpty()){
                    save.saveSteamHashMap(steamTitles);

                    for(Map.Entry<String,Boolean> entry :steamTitles.entrySet()){
                        String title = entry.getKey();

                        if(checkHLTB(title)){
                            entry.setValue(true);
                        }

                    }
                }

                loadSteamLabel.setText("Loaded " + steamTitles.size() + " titles from your Steam library");
                loadSteamOKButton.setVisible(false);
                loadSteamCancelButton.setVisible(false);
                loadSteamDoneButton.setVisible(true);
                loadSteamDoneButton.setDisable(false);
            }
        });

        loadSteamCancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) loadSteamCancelButton.getScene().getWindow();
                stage.close();
            }
        });

        loadSteamDoneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                driver.close();
                Stage stage = (Stage) loadSteamDoneButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    public boolean checkHLTB(String name){
        String stringHLTBRating;
        String stringRating;
        hltb.searchGameName(name);
        stringRating = hltb.getGameRating();
        if(stringRating.equals("0")){
            stringHLTBRating = "0";
        }else{
           stringHLTBRating = hltb.getGameHLTB();
        }

        double rating = Double.parseDouble(stringRating);
        int hltbRating = Integer.parseInt(stringHLTBRating);
        conn =  DBConnection.startConnection();

        String sql = "INSERT INTO all_games (title,rating,gamingSystem,category,hltb,notes,favorite) VALUES (?,?,?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1,name);
            preparedStatement.setDouble(2,rating);
            preparedStatement.setString(3,"Steam");
            preparedStatement.setString(4,"Unsorted");
            preparedStatement.setInt(5,hltbRating);
            preparedStatement.setString(6,"");
            preparedStatement.setBoolean(7,false);

            preparedStatement.execute();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            dialogPopUp("Stuck in check HLTB");
            return false;
        }
    }

    public void dialogPopUp(String text){
        Dialog<String> d = new Dialog<String>();
        d.setContentText(text);
        d.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        d.showAndWait();
    }
}
