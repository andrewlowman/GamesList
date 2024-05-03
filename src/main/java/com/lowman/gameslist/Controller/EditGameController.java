package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Model.Category;
import com.lowman.gameslist.Model.Game;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditGameController implements Initializable {
    public TextField editGameTitleTxtField;
    public TextField editGameRatingTxtField;
    public TextField editGameHLTBTxtField;
    public ChoiceBox<Category> editGameCategoryChoiceBox;
    public ChoiceBox<GamingSystem> editGameSystemChoiceBox;
    public Button editGameSaveButton;
    public Button editGameExitButton;
    public Button editGameDeleteButton;
    public AnchorPane editGameMainPanel;
    public TextArea editGameNotesTextArea;
    public CheckBox editGameFavoriteCheckBox;
    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private ObservableList<GamingSystem> systems = FXCollections.observableArrayList();
    private Game game;

    Connection conn = null;

    public EditGameController(Game newGame){
        this.game = newGame;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editGameTitleTxtField.setText(game.getTitle());
        editGameHLTBTxtField.setText(String.valueOf(game.getHowLongToBeat()));
        editGameNotesTextArea.setText(game.getNotes());
        editGameRatingTxtField.setText(String.valueOf(game.getRating()));

        //load choice boxes with enums
        for (Category c:Category.values()){
            categories.add(c);
        }
        editGameCategoryChoiceBox.setItems(categories);
        editGameCategoryChoiceBox.setValue(game.getCategory());

        for(GamingSystem g:GamingSystem.values()){
            systems.add(g);
        }

        editGameSystemChoiceBox.setItems(systems);
        editGameSystemChoiceBox.setValue(game.getGamingSystem());

        if(game.isFavorite()){
            editGameFavoriteCheckBox.setSelected(true);
        }

        editGameExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) editGameExitButton.getScene().getWindow();
                stage.close();
            }
        });

        editGameSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String title = editGameTitleTxtField.getText();
                if(title.equals("")||title == null){
                    dialogPopUp("Enter a title");
                    return;
                }

                ///check if number
                try{
                    Double.parseDouble(editGameRatingTxtField.getText());
                }catch (Exception e){
                    dialogPopUp("The rating must be a number");
                    return;
                }
                double rating = Double.parseDouble(editGameRatingTxtField.getText());
                if(rating<0||rating>10){
                    dialogPopUp("Rating must be between 0 and 10");
                    return;
                }

                try{
                    Integer.parseInt(editGameHLTBTxtField.getText());
                }catch(Exception e){
                    dialogPopUp("How long to beat must be a number");
                    return;
                }
                int hltb = Integer.parseInt(editGameHLTBTxtField.getText());
                if(hltb<=0){
                    dialogPopUp("How long to beat must be above zero");
                    return;
                }

                String gamingSystem = editGameSystemChoiceBox.getValue().toString();
                String category = editGameCategoryChoiceBox.getValue().toString();
                String notes = editGameNotesTextArea.getText();
                boolean favorite;
                if(editGameFavoriteCheckBox.isSelected()){
                    favorite = true;
                }else{
                    favorite = false;
                }

                conn =  DBConnection.startConnection();

                String sql = "UPDATE all_games SET title=?, rating=?,gamingSystem=?,category=?,hltb=?,notes=?,favorite=? WHERE id_number =?";
                try {

                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,title);
                    preparedStatement.setDouble(2,rating);
                    preparedStatement.setString(3,gamingSystem);
                    preparedStatement.setString(4,category);
                    preparedStatement.setInt(5,hltb);
                    preparedStatement.setString(6,notes);
                    preparedStatement.setBoolean(7,favorite);
                    preparedStatement.setInt(8,game.getIdNumber());

                    preparedStatement.executeUpdate();

                    DBConnection.closeConnection();

                } catch (SQLException e) {
                    e.printStackTrace();
                    dialogPopUp("Something went wrong");
                }

                Stage stage = (Stage) editGameSaveButton.getScene().getWindow();
                stage.close();
            }
        });

        editGameDeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to delete this game?");

                Optional<ButtonType> end = alert.showAndWait();
                ButtonType button = end.orElse(ButtonType.NO);

                if(button == ButtonType.OK){

                    try {
                        remove(game.getIdNumber());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else if(button == ButtonType.CANCEL){
                    alert.close();
                }

                Stage stage = (Stage) editGameSaveButton.getScene().getWindow();
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

    private void remove(int idNumber) throws SQLException {

        conn = DBConnection.startConnection();

        String sql = "DELETE FROM all_games WHERE id_number = " + idNumber;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();

        DBConnection.closeConnection();
    }
}
