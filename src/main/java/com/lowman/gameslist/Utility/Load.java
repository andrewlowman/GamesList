package com.lowman.gameslist.Utility;

import com.lowman.gameslist.Model.Game;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Load implements Serializable {
    //class for loading todo list

    public HashMap<Integer, Game> loadSearchHashMap(){
        HashMap<Integer,Game> map = null;
        try{
            FileInputStream fileInputStream = new FileInputStream("/searchList.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (HashMap<Integer, Game>) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }
}