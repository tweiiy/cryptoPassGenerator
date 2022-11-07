package com.tweiiy.cryptopassgenerator;

public class SavedPassword {
    private String password;
    private String comment;

    public SavedPassword(String password, String comment) {
        this.password = password;
        this.comment = comment;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public String getComment() {
        return comment;
    }
}
