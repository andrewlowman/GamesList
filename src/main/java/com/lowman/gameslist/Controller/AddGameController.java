package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Model.Category;
import com.lowman.gameslist.Model.GamingSystem;
import com.lowman.gameslist.Utility.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.ConnectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddGameController implements Initializable {
    public TextField addGameTitleTxtField;
    public TextField addGameRatingTxtField;
    public TextField addGameHLTBTxtField;
    public ChoiceBox<Category> addGameCategoryChoiceBox;
    public ChoiceBox<GamingSystem> addGameSystemChoiceBox;
    public Button addGameSaveButton;
    public Button addGameDeleteButton;
    public Button addGameExitButton;
    public AnchorPane addGameMainPanel;
    public TextArea addGameNotesTextArea;
    public CheckBox addGameFavoriteCheckBox;
    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private ObservableList<GamingSystem> systems = FXCollections.observableArrayList();

    Connection conn = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //load choice boxes with enums
        for (Category c:Category.values()){
            categories.add(c);
        }
        addGameCategoryChoiceBox.setItems(categories);
        addGameCategoryChoiceBox.setValue(Category.Unsorted);

        for(GamingSystem g:GamingSystem.values()){
            systems.add(g);
        }
        addGameSystemChoiceBox.setItems(systems);
        addGameSystemChoiceBox.setValue(GamingSystem.Unknown);

        //save button to insert into database
        addGameSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String title = addGameTitleTxtField.getText();
                if(title.equals("")||title == null){
                    dialogPopUp("Enter a title");
                    return;
                }

                ///check if number
                try{
                    Double.parseDouble(addGameRatingTxtField.getText());
                }catch (Exception e){
                    dialogPopUp("The rating must be a number");
                    return;
                }
                double rating = Double.parseDouble(addGameRatingTxtField.getText());
                if(rating<0||rating>10){
                    dialogPopUp("Rating must be between 0 and 10");
                    return;
                }

                try{
                    Integer.parseInt(addGameHLTBTxtField.getText());
                }catch(Exception e){
                    dialogPopUp("How long to beat must be a number");
                    return;
                }
                int hltb = Integer.parseInt(addGameHLTBTxtField.getText());
                if(hltb<=0){
                    dialogPopUp("How long to beat must be above zero");
                    return;
                }

                String gamingSystem = addGameSystemChoiceBox.getValue().toString();
                String category = addGameCategoryChoiceBox.getValue().toString();
                String notes = addGameNotesTextArea.getText();
                boolean favorite;
                if(addGameFavoriteCheckBox.isSelected()){
                    favorite = true;
                }else{
                    favorite = false;
                }

                conn =  DBConnection.startConnection();

                String sql = "INSERT INTO all_games (title,rating,gamingSystem,category,hltb,notes,favorite) VALUES (?,?,?,?,?,?,?)";
                try {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,title);
                    preparedStatement.setDouble(2,rating);
                    preparedStatement.setString(3,gamingSystem);
                    preparedStatement.setString(4,category);
                    preparedStatement.setInt(5,hltb);
                    preparedStatement.setString(6,notes);
                    preparedStatement.setBoolean(7,favorite);

                    preparedStatement.execute();

                    DBConnection.closeConnection();

                } catch (SQLException e) {
                    e.printStackTrace();
                    dialogPopUp("Something went wrong");
                }

                Stage stage = (Stage) addGameSaveButton.getScene().getWindow();
                stage.close();
            }
        });

        addGameExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) addGameExitButton.getScene().getWindow();
                stage.close();
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
