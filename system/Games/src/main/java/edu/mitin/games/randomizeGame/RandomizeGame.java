package edu.mitin.games.randomizeGame;

import edu.mitin.games.Game;
import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;

import java.util.Random;


/**
 * Тестовая игра для тестирования
 * Игрокам дается случайное число от 1 до 100, сами они его не знают
 * Они должны угадать число соперника
 * По сути кто первый угадает загаданное число, класс будет отдавать 1 если загаданное число больше и -1 в противном случае
 * Тут нет взаимодействия между игроками!
 */
public class RandomizeGame implements Game {
    public RandomizeGame() {
    }

    Player player1;
    Integer randomPlayer1 = 44;//new Random().nextInt(100) + 1;
    Integer randomPlayer2 = new Random().nextInt(100) + 1;
    Player player2;

    ResultOfGame gameResult;

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
    public void registerPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        gameResult = new ResultOfGame(player1, player2);
    }

    @Override
    public String doAction(String action, Player playerWhoAttack) {
        int shot = Integer.parseInt(action);
        if (playerWhoAttack.equals(player1)) {
            gameResult.addLeftPlayerStep(String.valueOf(shot));
            if (shot < randomPlayer2) {
                return "-1";
            }
            if (shot > randomPlayer2) {
                return "1";
            }
            if (shot == randomPlayer2) {
                return "0";
            }
        } else if (playerWhoAttack.equals(player2)) {
            gameResult.addRightPlayerStep(String.valueOf(shot));
            if (shot < randomPlayer1) {
                return "-1";
            }
            if (shot > randomPlayer1) {
                return "1";
            }
            if (shot == randomPlayer1) {
                return "0";
            }
        }
        return null;
    }

    @Override
    public boolean isWinAction(String action, Player playerWhoAttack) {
        int shot = Integer.parseInt(action);
        boolean isWin;
        if (playerWhoAttack.equals(player1)) {
            isWin = randomPlayer2.equals(shot);
        } else if (playerWhoAttack.equals(player2)) {
            isWin = randomPlayer1.equals(shot);
        } else {
            throw new RuntimeException("Такого игрока нет");
        }
        if (isWin) {
            gameResult.setWinner(playerWhoAttack);
        }
        return isWin;
    }

    @Override
    public ResultOfGame getResultOfGame() {
        return gameResult;
    }

    private Integer getPlayerNumber(Player player) {
        if (player.equals(player1)) {
            return randomPlayer1;
        } else if (player.equals(player2)) {
            return randomPlayer2;
        } else {
            throw new RuntimeException("Такого игрока нет");
        }
    }
}
