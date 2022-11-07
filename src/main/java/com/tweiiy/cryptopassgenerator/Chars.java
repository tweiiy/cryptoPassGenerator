package com.tweiiy.cryptopassgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Chars {
    protected boolean isActive;
    public ArrayList chars;
    public boolean isActive() {
        return isActive;
    }
    public ArrayList getChars(){
        return chars;
    }
    public void setActive(boolean active) {
        isActive=active;
    }
    public Chars(){
        isActive = false;
    }
}