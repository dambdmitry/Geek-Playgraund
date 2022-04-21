package edu.mitin.games.service.model;

import edu.mitin.games.service.provider.exceptions.GameProviderException;

import java.util.ArrayList;
import java.util.List;

public class ResultOfGame {
    private String winner;
    private String leftPlayerName;
    private String rightPlayerName;

    private Failure failure;


    private List<String> leftPlayerSteps;
    private List<String> rightPlayerSteps;

    private Result result;

    public Failure getFailure() {
        return failure;
    }

    public ResultOfGame(Result result, String author, String description) {
        this.result = result;
        this.failure = new Failure(author, description);
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }


    public enum Result{
        DONE,
        ERROR
    }

    public ResultOfGame(String winner, String leftPlayer, String rightPlayer, List<String> leftPlayerSteps, List<String> rightPlayerSteps) {
        this.winner = winner;
        this.leftPlayerName = leftPlayer;
        this.rightPlayerName = rightPlayer;
        this.leftPlayerSteps = leftPlayerSteps;
        this.rightPlayerSteps = rightPlayerSteps;
    }

    public ResultOfGame(String leftPlayer, String rightPlayer) {
        this.leftPlayerName = leftPlayer;
        this.rightPlayerName = rightPlayer;
        this.leftPlayerSteps = new ArrayList<>();
        this.rightPlayerSteps = new ArrayList<>();
    }


    public void setLeftPlayerName(String leftPlayerName) {
        this.leftPlayerName = leftPlayerName;
    }

    public void setRightPlayerName(String rightPlayerName) {
        this.rightPlayerName = rightPlayerName;
    }

    public void addLeftPlayerStep(String step) {
        leftPlayerSteps.add(step);
    }

    public void addRightPlayerStep(String step) {
        rightPlayerSteps.add(step);
    }


    @Override
    public String toString() {
        return "ResultOfGame{" +
                "winner='" + winner + '\'' +
                ", leftPlayerName='" + leftPlayerName + '\'' +
                ", rightPlayerName='" + rightPlayerName + '\'' +
                ", leftPlayerSteps=" + leftPlayerSteps +
                ", rightPlayerSteps=" + rightPlayerSteps +
                ", result=" + result;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLeftPlayerName() {
        return leftPlayerName;
    }

    public String getRightPlayerName() {
        return rightPlayerName;
    }

    public List<String> getLeftPlayerSteps() {
        return leftPlayerSteps;
    }

    public List<String> getRightPlayerSteps() {
        return rightPlayerSteps;
    }

}
