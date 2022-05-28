package edu.mitin.performance.model;

import edu.mitin.performance.factory.Compiler;

import java.io.File;

public class PlayerModel {
    private String playerName;
    private String code;
    private Compiler compiler;
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

    public Compiler getCompiler() {
        return compiler;
    }

    public void setCompiler(Compiler compiler) {
        this.compiler = compiler;
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
