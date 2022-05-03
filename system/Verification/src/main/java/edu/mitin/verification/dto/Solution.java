package edu.mitin.verification.dto;

public class Solution {
    private String playerName;
    private String language;
    private String code;
    private String game;
    private Long tournamentId;

    public Solution(String playerName, String language, String code, String game, Long tournamentId) {
        this.playerName = playerName;
        this.language = language;
        this.code = code;
        this.game = game;
        this.tournamentId = tournamentId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public String getGame() {
        return game;
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
