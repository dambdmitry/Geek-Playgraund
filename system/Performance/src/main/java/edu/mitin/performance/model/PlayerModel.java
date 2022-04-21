package edu.mitin.performance.model;

import edu.mitin.games.service.model.Language;

import java.io.File;

public class PlayerModel {
    private String playerName;
    private String code;
    private Language language;
    private File programFile;
    private String[] commandToStart;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public File getProgramFile() {
        return programFile;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setProgramFile(File programFile) {
        this.programFile = programFile;
    }

    public String[] getCommandToStart() {
        return commandToStart;
    }

    public void setCommandToStart(String[] commandToStart) {
        this.commandToStart = commandToStart;
    }
}
