package edu.mitin.games;

import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;

//TODO переделать в абстрактный класс
public interface Game {
    boolean isCorrectAction(String action);

    /**
     * Регистрация игроков, порядок важен - player1 ходит первый
     * @param player1
     * @param player2
     */
    void registerPlayers(Player player1, Player player2);

    /**
     *
     * @param action
     * @param playerWhoAttack
     * @return результат хода игрока, отдается классом игры (опционален), например в морском бое УБИЛ, РАНИЛ, МИМО. Или например в игре змейка ответ от класса не нужен, он идет от игрока.
     */
    String doAction(String action, Player playerWhoAttack);

    boolean isWinAction(String action, Player playerWhoAttack);

    ResultOfGame getResultOfGame();
}
