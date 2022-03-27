package edu.mitin.verification.model;

public class PlayerSolution {
    private String playerName;
    private String language;
    private String code;

    public PlayerSolution(String playerName, String language, String code) {
        this.playerName = playerName;
        this.language = language;
        this.code = code;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }
}
