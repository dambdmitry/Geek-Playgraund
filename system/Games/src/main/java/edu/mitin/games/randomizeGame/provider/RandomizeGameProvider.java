package edu.mitin.games.randomizeGame.provider;

import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.games.service.provider.GameProvider;
import edu.mitin.games.service.provider.exceptions.GameProviderException;
import edu.mitin.games.randomizeGame.game.RandomizeGame;

import java.util.List;

/**
 * Пример реализации провайдера игры
 */
public class RandomizeGameProvider extends GameProvider {

    /**
     * Обязательно вызвать конструктор родителя
     * @param leftPlayer игрок 1
     * @param rightPlayer игрок 2
     * пока остановимся на хардкоде игры провайдера.
     */
    public RandomizeGameProvider(Player leftPlayer, Player rightPlayer) {
        super(leftPlayer, rightPlayer, new RandomizeGame(leftPlayer.getName(), rightPlayer.getName()));
    }

    //Пример запуска игры
    public static void main(String[] args) throws Exception {
        Player player1 = new Player("one", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution1.java"});
        Player player2 = new Player("two", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution2.java"});
        ResultOfGame resultOfGame = new RandomizeGameProvider(player1, player2).executeGame();
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
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }

        //ИГРА
        //Чтение первых ходов
        String actionsSeparator = "\r\n"; //разделитель ходов, которые игрок "пишет" в файл
        String leftPlayerAction = null;
        String rightPlayerAction = null;
        try {
            leftPlayerAction = readLeftPlayerAction(actionsSeparator, 1).get(0);
            rightPlayerAction = readRightPlayerAction(actionsSeparator, 1).get(0);
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }

        System.out.println(leftPlayerAction + "---" + rightPlayerAction);

        try {
            checkIsAliveProcesses(); // проверим не сломались ли процессы игры
        } catch (GameProviderException e) {
            //Чистка за собой
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }

        //Проверка того, что игроки прислали корректные ходы в контексте игры
        if (!game.isCorrectAction(leftPlayerAction) || !game.isCorrectAction(rightPlayerAction)) {
            String author = !game.isCorrectAction(leftPlayerAction) ? leftPlayer.getName() : rightPlayer.getName();
            String action = !game.isCorrectAction(leftPlayerAction) ? leftPlayerAction : rightPlayerAction;
            final String message = "Некорректное действие " + action;
            System.out.println(message);
            return getFailedResultOfGame(new GameProviderException(message, author));
        }

        //Цикл игры
        while (!game.isWinLeftPlayerAction(leftPlayerAction) && !game.isWinRightPlayerAction(rightPlayerAction)) {
            String answerToLeftPlayer = game.executeLeftPlayerAction(leftPlayerAction) + actionsSeparator;
            String answerToRightPlayer = game.executeRightPlayerAction(rightPlayerAction) + actionsSeparator;

            //Отправка ответов на ходы игрокам
            sendToLeftPlayer(List.of(answerToLeftPlayer));
            sendToRightPlayer(List.of(answerToRightPlayer));

            //Чтение новых ходов
            try {
                leftPlayerAction = readLeftPlayerAction(actionsSeparator, 1).get(0);
                rightPlayerAction = readRightPlayerAction(actionsSeparator, 1).get(0);
            } catch (GameProviderException e) {
                System.out.println(e.getMessage());
                return getFailedResultOfGame(e);
            }

            System.out.println(leftPlayerAction + "---" + rightPlayerAction);

            if(isHanged(60)) {
                System.out.println("зациклено");
                return getFailedResultOfGame(new GameProviderException("Игра преодолела лимит по времени", "Игра"));
            }
        }


        //Чистка за собой
        closeAndDeleteProcesses();

        System.out.println("winner is " + game.getResultOfGame().getWinner());
        return game.getResultOfGame();
    }
}
