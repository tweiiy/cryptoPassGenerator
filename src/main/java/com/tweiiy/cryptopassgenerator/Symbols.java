package com.tweiiy.cryptopassgenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class Symbols extends Chars {
    public Symbols(){
        super();
        chars = new ArrayList(Arrays.asList('`','~','!','#','№','$','%','^','&','?','*','-','_','(',')','=','+','/','\\','.',"'",'"',';',':','[',']','{','}','<','>'));
    }
}
