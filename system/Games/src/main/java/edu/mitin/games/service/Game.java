package edu.mitin.games.service;

import edu.mitin.games.service.model.ResultOfGame;

public abstract class Game {
    protected String leftPlayerName;
    protected String rightPlayerName;
    protected ResultOfGame gameResult;

    public Game(String leftPlayerName, String rightPlayerName) {
        this.gameResult = new ResultOfGame(leftPlayerName, rightPlayerName);
    }

    public Game() {
    }

    public void registerPlayers(String leftPlayerName, String rightPlayerName) {
        this.leftPlayerName = leftPlayerName;
        this.rightPlayerName = rightPlayerName;

    }

    public ResultOfGame getResultOfGame(){
        gameResult.setResult(ResultOfGame.Result.DONE);
        return gameResult;
    }

    public abstract boolean isCorrectAction(String action);

    public abstract String executeLeftPlayerAction(String action);
    public abstract String executeRightPlayerAction(String action);

    public abstract boolean isWinLeftPlayerAction(String action);
    public abstract boolean isWinRightPlayerAction(String action);
}
