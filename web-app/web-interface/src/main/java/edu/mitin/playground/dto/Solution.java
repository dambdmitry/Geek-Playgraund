package edu.mitin.playground.dto;

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

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
