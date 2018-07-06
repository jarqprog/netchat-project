package com.codecool.networking.modes;

public enum MagicWords {

    QUIT_CHAT_WORD(".END");

    private String word;

    MagicWords(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
