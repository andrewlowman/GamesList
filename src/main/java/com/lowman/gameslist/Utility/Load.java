package com.lowman.gameslist.Utility;

import com.lowman.gameslist.Model.Game;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Load implements Serializable {
    //class for loading todo list

    public HashMap<String, Boolean> loadSteamHashMap(){
        HashMap<String,Boolean> map = null;
        try{
            FileInputStream fileInputStream = new FileInputStream("steamHashMap.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (HashMap<String, Boolean>) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}