package com.lowman.gameslist.Utility;

import com.lowman.gameslist.Model.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Save implements Serializable {
    public void saveSearchHashMap(HashMap<String, String> map){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("/searchHashMap.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
