package com.lowman.gameslist.Model;

public class Game {

    private String title;
    private double rating;

    private int HowLongToBeat;

    private Category category;
    private GamingSystem gamingSystem;
    private String notes;
    private boolean favorite;
    private int idNumber;

    public Game(String title, double rating, int howLongToBeat, int idNumber) {
        this.title = title;
        this.rating = rating;
        HowLongToBeat = howLongToBeat;
        category = Category.Unsorted;
        gamingSystem = GamingSystem.Unknown;
        this.idNumber = idNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getHowLongToBeat() {
        return HowLongToBeat;
    }

    public void setHowLongToBeat(int howLongToBeat) {
        HowLongToBeat = howLongToBeat;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public GamingSystem getGamingSystem() {
        return gamingSystem;
    }

    public void setGamingSystem(GamingSystem gamingSystem) {
        this.gamingSystem = gamingSystem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addNotes(String text){
        String add = " " + text;
        this.notes += add;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                '}';
    }
}
