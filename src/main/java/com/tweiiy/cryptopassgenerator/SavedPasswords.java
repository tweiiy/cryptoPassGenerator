package com.tweiiy.cryptopassgenerator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class SavedPasswords {
    private static ObservableList<SavedPassword> passwords = FXCollections.observableArrayList();

    public static ObservableList<SavedPassword> getPasswords() {
        return passwords;
    }

    public static void fillPasswords(File file, String key) throws IOException {
        if(!file.exists())
            file.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        try {
            line=br.readLine();
            String [] splitLine;
            while (line != null){
                if (!line.isEmpty()){
                    splitLine = line.split(" ", 2);
                    passwords.add(new SavedPassword(AesCipher.decrypt(key, splitLine[0]),splitLine[1]));
                    line=br.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            br.close();
        }
    }

    public static void addPass(File file, SavedPassword password) throws IOException {
        if(!file.exists())
            file.createNewFile();
        passwords.add(password);
    }

    public static void removePass(File file, SavedPassword password) throws IOException {
        if(!file.exists())
            file.createNewFile();
        passwords.remove(password);
    }

    public static void showPasswords() {
        for(SavedPassword password: passwords){
            System.out.println("password: "+password.getPassword()+" comment:"+password.getComment());
        }
    }
}
