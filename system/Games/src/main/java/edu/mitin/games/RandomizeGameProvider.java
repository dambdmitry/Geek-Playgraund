package edu.mitin.games;

import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;
import edu.mitin.games.provider.GameProviderAbs;
import edu.mitin.games.provider.exceptions.GameProviderException;
import edu.mitin.games.randomizeGame.RandomizeGame;

import java.io.IOException;
import java.util.List;

/**
 * Пример реализации провайдера игры
 */
public class RandomizeGameProvider extends GameProviderAbs {

    /**
     * Обязательно вызвать конструктор родителя
     * @param leftPlayer игрок 1
     * @param rightPlayer игрок 2
     * @param game игра, с которой идет взаимодействие
     */
    public RandomizeGameProvider(Player leftPlayer, Player rightPlayer, Game game) {
        super(leftPlayer, rightPlayer, game);
    }

    //Пример запуска игры
    public static void main(String[] args) throws Exception {
        Player player1 = new Player("one", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution1.java"});
        Player player2 = new Player("two", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution2.java"});
        ResultOfGame resultOfGame = new RandomizeGameProvider(player1, player2, new RandomizeGame()).executeGame();
        System.out.println();
        System.out.println(resultOfGame);

    }

    /*
        Метод для переопределения, в котором нужно управлять ходом игры
        с помощь унаследованных методов
        пример их использования приведен ниже
     */
    @Override
    public ResultOfGame executeGame() {
        //Инициализация полей игры, обязательный шаг.
        try {
            initGame();
        } catch (GameProviderException e) {
            e.printStackTrace();
        }

        //ИГРА
        //Чтение первых ходов
        String actionsSeparator = "\r\n"; //разделитель ходов, которые игрок "пишет" в файл
        String leftPlayerAction = readLeftPlayerAction(actionsSeparator, 1).get(0);
        String rightPlayerAction = readRightPlayerAction(actionsSeparator, 1).get(0);
        System.out.println(leftPlayerAction + "---" + rightPlayerAction);

        try {
            checkIsAliveProcesses(); // проверим не сломались ли процессы игры
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return null;
        }

        //Проверка того, что игроки прислали корректные ходы в контексте игры
        if (!game.isCorrectAction(leftPlayerAction) || !game.isCorrectAction(rightPlayerAction)) {
            throw new RuntimeException("Некорректное действие " + leftPlayerAction + " " + rightPlayerAction);
        }

        //Цикл игры
        while (!game.isWinAction(leftPlayerAction, leftPlayer) && !game.isWinAction(rightPlayerAction, rightPlayer)) {
            String answerToLeftPlayer = game.doAction(leftPlayerAction, leftPlayer) + actionsSeparator;
            String answerToRightPlayer = game.doAction(rightPlayerAction, rightPlayer) + actionsSeparator;

            //Отправка ответов на ходы игрокам
            sendToLeftPlayer(List.of(answerToLeftPlayer));
            sendToRightPlayer(List.of(answerToRightPlayer));

            //Чтение новых ходов
            leftPlayerAction = readLeftPlayerAction(actionsSeparator, 1).get(0);
            rightPlayerAction = readRightPlayerAction(actionsSeparator, 1).get(0);
            System.out.println(leftPlayerAction + "---" + rightPlayerAction);
        }


        //Чистка за собой
        closeAndDeleteProcesses();

        System.out.println("winner is " + game.getResultOfGame().getWinner().getName());
        return game.getResultOfGame();
    }
}
