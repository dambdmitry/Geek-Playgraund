package edu.mitin.performance.model;

import edu.mitin.storage.entity.Solution;

import java.util.List;

public class Tournament {
    private String gameName;
    private List<Solution> solutions;

    public Tournament() {
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
}
