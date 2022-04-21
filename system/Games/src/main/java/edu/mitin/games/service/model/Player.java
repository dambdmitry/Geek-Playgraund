package edu.mitin.games.service.model;

public class Player {

    String name;

    String[] commandToStartProgram;

    public String[] getCommandToStartProgram() {
        return commandToStartProgram;
    }

    public void setCommandToStartProgram(String[] commandToStartProgram) {
        this.commandToStartProgram = commandToStartProgram;
    }

    public Player(String name, String[] commandToStartProgram) {
        this.name = name;
        this.commandToStartProgram = commandToStartProgram;
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
