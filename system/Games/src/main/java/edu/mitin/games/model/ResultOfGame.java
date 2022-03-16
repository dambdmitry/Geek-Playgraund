package edu.mitin.games.model;

import java.util.ArrayList;
import java.util.List;

public class ResultOfGame {
    private Player winner;
    private Player leftPlayer;
    private Player rightPlayer;

    private List<String> leftPlayerSteps;
    private List<String> rightPlayerSteps;

    public ResultOfGame() {
    }

    public ResultOfGame(Player winner, Player leftPlayer, Player rightPlayer, List<String> leftPlayerSteps, List<String> rightPlayerSteps) {
        this.winner = winner;
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        this.leftPlayerSteps = leftPlayerSteps;
        this.rightPlayerSteps = rightPlayerSteps;
    }

    public ResultOfGame(Player leftPlayer, Player rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        this.leftPlayerSteps = new ArrayList<>();
        this.rightPlayerSteps = new ArrayList<>();
    }

    public void addLeftPlayerStep(String step) {
        leftPlayerSteps.add(step);
    }

    public void addRightPlayerStep(String step) {
        rightPlayerSteps.add(step);
    }


    public String toString(){
        String res = "";
        res += "Ходы игрока " + leftPlayer.getName() + ":\n";
        int i = 1;
        for (String step : leftPlayerSteps){
            res += i + ") " + step + "\n";
            i++;
        }
        res += "Ходы игрока " + rightPlayer.getName() + ":\n";
        i = 1;
        for (String step : rightPlayerSteps){
            res += i + ") " + step + "\n";
            i++;
        }
        res += "Победитель: " + winner.getName();
        return res;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLeftPlayer() {
        return leftPlayer;
    }

    public void setLeftPlayer(Player leftPlayer) {
        this.leftPlayer = leftPlayer;
    }

    public Player getRightPlayer() {
        return rightPlayer;
    }

    public void setRightPlayer(Player rightPlayer) {
        this.rightPlayer = rightPlayer;
    }

    public List<String> getLeftPlayerSteps() {
        return leftPlayerSteps;
    }

    public void setLeftPlayerSteps(List<String> leftPlayerSteps) {
        this.leftPlayerSteps = leftPlayerSteps;
    }

    public List<String> getRightPlayerSteps() {
        return rightPlayerSteps;
    }

    public void setRightPlayerSteps(List<String> rightPlayerSteps) {
        this.rightPlayerSteps = rightPlayerSteps;
    }
}
