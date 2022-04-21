package edu.mitin.verification.dto;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.games.service.model.Language;

public class Solution {
    private String playerName;
    private Language language;
    private String code;
    private String game;
    private Long tournamentId;

    public Solution(String playerName, Language language, String code, String game, Long tournamentId) {
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

    public Language getLanguage() {
        return language;
    }

    public String getCode() {
        return code;
    }
}
