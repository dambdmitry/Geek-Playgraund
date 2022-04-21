package edu.mitin.games.randomizeGame.game;

import edu.mitin.games.service.Game;

public class RandomizeGame extends Game {

    private final Integer randomLeftPlayer = (int) (Math.random()*(100+1));
    private final Integer randomRightPlayer = (int) (Math.random()*(100+1));


    public RandomizeGame(String leftPlayerName, String rightPlayerName) {
        super(leftPlayerName, rightPlayerName);
    }

    @Override
    public boolean isCorrectAction(String action) {
        try{
            int shot = Integer.parseInt(action);
            return shot >= 1 && shot <= 100;
        } catch (NumberFormatException exception){
            return false;
        }
    }

    @Override
    public String executeLeftPlayerAction(String action) {
        Integer shot = Integer.parseInt(action);
        return String.valueOf(shot.compareTo(randomRightPlayer));
    }

    @Override
    public String executeRightPlayerAction(String action) {
        Integer shot = Integer.parseInt(action);
        return String.valueOf(shot.compareTo(randomLeftPlayer));
    }

    @Override
    public boolean isWinLeftPlayerAction(String action) {
        int shot = Integer.parseInt(action);
        gameResult.addLeftPlayerStep(action);
        boolean isWin = randomRightPlayer.equals(shot);
        if (isWin) {
            gameResult.setWinner(leftPlayerName);
        }
        return isWin;
    }

    @Override
    public boolean isWinRightPlayerAction(String action) {
        int shot = Integer.parseInt(action);
        gameResult.addRightPlayerStep(action);
        boolean isWin = randomLeftPlayer.equals(shot);
        if (isWin) {
            gameResult.setWinner(rightPlayerName);
        }
        return isWin;
    }
}
