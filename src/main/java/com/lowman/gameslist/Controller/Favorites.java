package com.lowman.gameslist.Controller;

import com.lowman.gameslist.Model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Favorites {

    private ObservableList<Game> favoritesList = FXCollections.observableArrayList();

    public Favorites(){

    }

    public ObservableList<Game> getFavoritesList() {
        return favoritesList;
    }

    public void setFavoritesList(ObservableList<Game> favoritesList) {
        this.favoritesList = favoritesList;
    }
}
