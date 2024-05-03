package com.lowman.gameslist;

import com.lowman.gameslist.Controller.AddGameController;
import com.lowman.gameslist.Controller.EditGameController;
import com.lowman.gameslist.Controller.Favorites;
import com.lowman.gameslist.Model.Category;
import com.lowman.gameslist.Model.Game;
import com.lowman.gameslist.Model.GamingSystem;
import com.lowman.gameslist.Utility.DBConnection;
import com.lowman.gameslist.Utility.Load;
import com.lowman.gameslist.Utility.Save;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class MainController implements Initializable {

    public ScrollPane middleScrollPane;

    public ScrollPane leftScrollPane;
    public TableView<Game> mainTable;
    public TableColumn<Game,String> mainTitleCol;
    public TableColumn<Game, GamingSystem> mainSystemCol;
    public TableColumn<Game, Integer> mainHLTBCol;
    public TableColumn<Game, Double> mainRatingCol;
    public TableColumn<Game,String> mainNotesCol;
    public AnchorPane mainPanel;
    public TableColumn<Game,Category> mainCategoryCol;
    public MenuBar mainMenuBar;
    public MenuItem mainExitMenuItem;
    public MenuItem menuAddGameMenuItem;
    public HBox mainHBox;
    public TableColumn<Game,String> sideTableTitleCol;
    public TableColumn<Game,GamingSystem> sideTableSystemCol;
    public TableColumn<Game,Integer> sideTableHLTBCol;
    public TableColumn<Game,Double> sideTableRatingCol;
    public TableColumn<Game,Category> sideTableCategoryCol;
    public TableColumn<Game,String> sideTableNotesCol;
    public TextField searchTitleTextField;
    public ChoiceBox<GamingSystem> searchSystemChoiceBox;
    public ChoiceBox<Category> searchCategoryChoiceBox;
    public Spinner<Integer> searchHLTBSpinner;
    public Button searchSearchButton;
    public TableColumn<Game,Boolean> sideTableFavoriteCol;
    public TableView<Game> searchTable;
    public TableColumn<Game, Boolean> mainFavoriteCol;
    public CheckBox searchFavoriteCheckBox;
    public MenuItem menuItemFavorites;
    public MenuItem menuItemMustPlay;
    public MenuItem menuItemTryOut;
    public MenuItem menuItemFinished;
    public MenuItem menuItemNoInterest;
    public MenuItem menuItemUnsorted;
    public Spinner<Double> searchRatingSpinner;
    public CheckBox searchTitleCheckBox;
    public CheckBox searchRatingCheckBox;
    public CheckBox searchSystemCheckBox;
    public CheckBox searchCategoryCheckBox;
    public CheckBox searchHLTBCheckBox;
    public MenuItem saveSearchMenuItem;
    public MenuItem menuItemPS5;
    public MenuItem menuItemPS4;
    public MenuItem menuItemSwitch;
    public MenuItem menuItemSteam;
    public MenuItem menuItemEpic;
    public MenuItem menuItemAmazon;
    public MenuItem menuItemItchio;
    public MenuItem menuItemUbisoft;
    public MenuItem menuItemEA;
    public MenuItem menuItemMicrosoft;
    public MenuItem menuItemUnknown;
    public MenuItem menuItemDelete;
    public MenuItem menuItemEditGame;
    private Connection conn = null;
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<GamingSystem> systems = FXCollections.observableArrayList();
    private final ObservableList<Game> allGames = FXCollections.observableArrayList();
    private final ObservableList<Game> favorites = FXCollections.observableArrayList();
    private final ObservableList<Game> searchList = FXCollections.observableArrayList();
    private String saveSQL = "";
    private HashMap<String,String> integerGameHashMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("/searchList.ser");
        Save save = new Save();
        Load load = new Load();

        //load choice boxes
        systems.addAll(Arrays.asList(GamingSystem.values()));

        categories.addAll(Arrays.asList(Category.values()));
        searchSystemChoiceBox.setItems(systems);
        searchSystemChoiceBox.setValue(GamingSystem.Unknown);
        searchCategoryChoiceBox.setItems(categories);
        searchCategoryChoiceBox.setValue(Category.Unsorted);

        //set values for spinner
        searchHLTBSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2000));

        //set checkboxes to activate elements
        searchCategoryCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(searchCategoryChoiceBox.isDisabled()){
                    searchCategoryChoiceBox.setDisable(false);
                }else{
                    searchCategoryChoiceBox.setDisable(true);
                }
            }
        });

        searchHLTBCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(searchHLTBSpinner.isDisabled()){
                    searchHLTBSpinner.setDisable(false);
                }else{
                    searchHLTBSpinner.setDisable(true);
                }
            }
        });

        searchRatingCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(searchRatingSpinner.isDisabled()){
                    searchRatingSpinner.setDisable(false);
                }else{
                    searchRatingSpinner.setDisable(true);
                }
            }
        });

        searchSystemCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(searchSystemChoiceBox.isDisabled()){
                    searchSystemChoiceBox.setDisable(false);
                }else{
                    searchSystemChoiceBox.setDisable(true);
                }
            }
        });

        searchTitleCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(searchTitleTextField.isDisabled()){
                    searchTitleTextField.setDisable(false);
                }else{
                    searchTitleTextField.setDisable(true);
                }
            }
        });

        //searchRatingSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1,10));

        //set cells in tables
        mainTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        mainRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        mainSystemCol.setCellValueFactory(new PropertyValueFactory<>("gamingSystem"));
        mainRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        mainHLTBCol.setCellValueFactory(new PropertyValueFactory<>("HowLongToBeat"));
        mainNotesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        mainCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        mainFavoriteCol.setCellValueFactory(new PropertyValueFactory<>("favorite"));
        mainTable.setItems(allGames);

        sideTableTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        sideTableRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        sideTableSystemCol.setCellValueFactory(new PropertyValueFactory<>("gamingSystem"));
        sideTableRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        sideTableHLTBCol.setCellValueFactory(new PropertyValueFactory<>("HowLongToBeat"));
        sideTableNotesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        sideTableCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        sideTableFavoriteCol.setCellValueFactory(new PropertyValueFactory<>("favorite"));

        //set message for empty tables
        mainTable.setPlaceholder(new Label("No results"));
        searchTable.setPlaceholder(new Label("No results"));

        //exit
        mainExitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        //opens the add game window
        menuAddGameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addgame.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage newstage = new Stage();
                    newstage.setScene(scene);
                    newstage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        saveSearchMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(saveSQL != ""){
                   // save.saveSearchHashMap(integerGameHashMap);
                }
            }
        });

        //search for games list and fill the right table
        searchSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //check if search can actually be run
                if(!checkIfSearchAvailable()){
                    dialogPopUp("You have to select something to search for");
                    return;
                }

                searchList.clear();
                searchTable.setItems(searchList);
                System.out.println(search());
                try {
                    loadListFromSql(searchList,search());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //open favorites on right table
        menuItemFavorites.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                favorites.clear();
                searchTable.setItems(favorites);

                String sql = "SELECT * FROM all_games WHERE favorite = true";

                try {
                    loadListFromSql(favorites,sql);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        menuItemAmazon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Amazon'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemEA.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'EA'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });
        menuItemEpic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Epic'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemItchio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Itchio'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemMicrosoft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Microsoft'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemPS4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'PS4'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemUnknown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Unknown'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemPS5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'PS5'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE gamingSystem = 'Switch'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });

        menuItemFinished.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE category = 'Finished'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemMustPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE category = 'MustPlay'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemNoInterest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE category = 'NoInterest'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemTryOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE category = 'TryOut'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemUnsorted.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchList.clear();
                searchTable.setItems(searchList);

                String sql = "SELECT * FROM all_games WHERE category = 'Unsorted'";

                try{
                    loadListFromSql(searchList,sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               delete();
            }
        });

        menuItemEditGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    //add selected game to controller to pass to window
                    Game tempGame = mainTable.getSelectionModel().getSelectedItem();
                    if(tempGame == null){
                        dialogPopUp("Select a game to edit");
                        return;
                    }
                    EditGameController editGameController = new EditGameController(tempGame);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editgame.fxml"));
                    fxmlLoader.setController(editGameController);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage newstage = new Stage();
                    newstage.setScene(scene);
                    newstage.initModality(Modality.APPLICATION_MODAL);
                    newstage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()== MouseButton.SECONDARY){
                   double x = mouseEvent.getX();
                   double y = mouseEvent.getY();

                    try{
                        //add selected game to controller to pass to window
                        Game tempGame = mainTable.getSelectionModel().getSelectedItem();
                        if(tempGame == null){
                            dialogPopUp("Select a game to edit");
                            return;
                        }
                        EditGameController editGameController = new EditGameController(tempGame);

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editgame.fxml"));
                        fxmlLoader.setController(editGameController);
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage newstage = new Stage();
                        //set location to where the mouse clicked
                        newstage.setX(x);
                        newstage.setY(y);
                        //so cant open multi windows
                        newstage.initModality(Modality.APPLICATION_MODAL);
                        newstage.setScene(scene);
                        newstage.show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //method to update the main table from the db when mouse goes over app
    public void refresh() throws SQLException {
        //need to clear list or it keeps adding repeats
        allGames.clear();
        String sql = "SELECT * FROM all_games";

        loadListFromSql(allGames,sql);

    }

    public void dialogPopUp(String text){
        Dialog<String> d = new Dialog<String>();
        d.setContentText(text);
        d.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));
        d.showAndWait();
    }

    public void tableDialogPopUp(){

    }

    //method to load lists from sql query
    //fills a provided observablelist
    private void loadListFromSql(ObservableList list, String sql) throws SQLException {
        conn = DBConnection.startConnection();

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String title = rs.getString("title");
            double rating = rs.getDouble("rating");
            GamingSystem gs = GamingSystem.valueOf(rs.getString("gamingSystem"));
            Category category = Category.valueOf(rs.getString("category"));
            int hltb = rs.getInt("hltb");
            String notes = rs.getString("notes");
            boolean favorite = rs.getBoolean("favorite");
            int idNumber = rs.getInt("id_number");

            Game newGame = new Game(title,rating,hltb,idNumber);
            newGame.setGamingSystem(gs);
            newGame.setCategory(category);
            newGame.setNotes(notes);
            newGame.setFavorite(favorite);

            list.add(newGame);
        }

        DBConnection.closeConnection();
    }

    private String search(){
        int ctr =0;

        StringBuilder sbSQL = new StringBuilder("SELECT * FROM all_games WHERE");

        String title = searchTitleTextField.getText();


        boolean favorite = false;
        if(searchFavoriteCheckBox.isSelected()){
            favorite = true;
        }

        if(searchTitleCheckBox.isSelected() && !title.equals("")){
            sbSQL.append(" title LIKE " + "'%" + title + "%'");
            ctr++;
        }

        if(searchRatingCheckBox.isSelected()){
            double rating = searchRatingSpinner.getValue();
            if(ctr > 0){
                sbSQL.append(" and");
            }

            sbSQL.append(" rating > " + rating);
            ctr++;
        }

        if(searchSystemCheckBox.isSelected()){
            String gamingSystem = searchSystemChoiceBox.getValue().toString();
            if(ctr > 0){
                sbSQL.append(" and");
            }

            sbSQL.append(" gamingSystem = " + "'" + gamingSystem + "'");
            ctr++;
        }

        if(searchCategoryCheckBox.isSelected()){
            String category = searchCategoryChoiceBox.getValue().toString();
            if(ctr > 0){
                sbSQL.append(" and");
            }

            sbSQL.append(" category = " + "'" + category + "'");
            ctr++;
        }

        if(searchHLTBCheckBox.isSelected()){
            int hltb = searchHLTBSpinner.getValue();
            if(ctr > 0){
                sbSQL.append(" and");
            }

            sbSQL.append(" hltb < " + hltb);
            ctr++;
        }

        if(searchFavoriteCheckBox.isSelected()){
            if(ctr > 0){
                sbSQL.append(" and");
            }

            sbSQL.append(" favorite = " + favorite);
        }
        saveSQL = sbSQL.toString();
        return sbSQL.toString();
    }

    private boolean checkIfSearchAvailable(){
       int ctr = 0;

        if(searchTitleCheckBox.isSelected() && searchTitleTextField.getText() != ""){
            ctr = 1;
        }

        if(searchRatingCheckBox.isSelected()){
            ctr = 1;
        }

        if(searchSystemCheckBox.isSelected()){
            ctr = 1;
        }

        if(searchCategoryCheckBox.isSelected()){
            ctr = 1;
        }

        if(searchHLTBCheckBox.isSelected()){
            ctr = 1;
        }

        if(searchFavoriteCheckBox.isSelected()){
            ctr = 1;
        }

        if(ctr == 1){
            return true;
        }else{
            return false;
        }
    }

    private void remove(int idNumber) throws SQLException {

        conn = DBConnection.startConnection();

        String sql = "DELETE FROM all_games WHERE id_number = " + idNumber;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();

        DBConnection.closeConnection();
    }

    private void delete(){
        Game tempGame = mainTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this game?");

        Optional<ButtonType> end = alert.showAndWait();
        ButtonType button = end.orElse(ButtonType.NO);

        if(button == ButtonType.OK){

            mainTable.setItems(allGames);
            allGames.remove(tempGame);

            try {
                remove(tempGame.getIdNumber());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}