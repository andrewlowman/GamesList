package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Model.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomDialogController implements Initializable {
    public AnchorPane dialogMainPanel;
    public Button dialogCancelButton;
    public Button dialogDeleteButton;
    public Button dialogEditButton;
    private Game game;

    public CustomDialogController(Game newGame){
        this.game = newGame;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogCancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) dialogCancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }
}
