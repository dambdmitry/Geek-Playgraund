package edu.mitin.games.providers;

import edu.mitin.games.lines.game.LinesGame;
import edu.mitin.games.service.Game;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.games.service.provider.GameProvider;
import edu.mitin.games.service.provider.exceptions.GameProviderException;

import java.util.List;

public class B2BProvider extends GameProvider {

    private static final String ACTIONS_SEPARATOR = "\r\n"; //разделитель ходов, которые игрок "пишет" в файл

    public static void main(String[] args) {
        Player player = new Player("test", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\lines\\java.java"});
        Player player1 = new Player("tesssst", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\lines\\java.java"});
        final ResultOfGame resultOfGame = new B2BProvider(player, player1, new LinesGame(player.getName(), player.getName())).executeGame();
        System.out.println(resultOfGame);
    }

    public B2BProvider(Player leftPlayer, Player rightPlayer, Game game) {
        super(leftPlayer, rightPlayer, game);
    }

    @Override
    public ResultOfGame executeGame() {
        try {
            initGame();
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }
        String leftPlayerAction = null;
        String rightPlayerAction = null;
        sendStartMessages();

        try {
            checkIsAliveProcesses(); // проверим не сломались ли процессы игры
        } catch (GameProviderException e) {
            //Чистка за собой
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }

        sendToLeftPlayer(List.of("-1 -1 -1 -1" + ACTIONS_SEPARATOR));

        try {
            leftPlayerAction = readLeftPlayerAction(ACTIONS_SEPARATOR, 1).get(0);
            System.out.println("left - " + leftPlayerAction);
            //Проверка того, что игроки прислали корректные ходы в контексте игры
            if (isInvalidActions(leftPlayerAction, leftPlayerAction)) {
                return getFailedResultOfGame(new GameProviderException("Некорректнное действие: " + leftPlayerAction, leftPlayer.getName()));
            }
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }
        String answerToRightPlayer = executeLeftPlayerAction(leftPlayerAction) + ACTIONS_SEPARATOR;
        sendResponseToLeftPlayer(answerToRightPlayer);

        System.out.println("left response - " + answerToRightPlayer);
        sendToRightPlayer(List.of(answerToRightPlayer));

        try {
            rightPlayerAction = readRightPlayerAction(ACTIONS_SEPARATOR, 1).get(0);
            System.out.println("right - " + rightPlayerAction);
            //Проверка того, что игроки прислали корректные ходы в контексте игры
            if (isInvalidActions(rightPlayerAction, rightPlayerAction)) {
                return getFailedResultOfGame(new GameProviderException("Некорректнное действие: " + rightPlayerAction, rightPlayer.getName()));
            }
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }
        String answerToLeftPlayer = executeRightPlayerAction(rightPlayerAction) + ACTIONS_SEPARATOR;
        sendResponseToRightPlayer(answerToLeftPlayer);
        System.out.println("right response - " + answerToLeftPlayer);


        while (!game.isStandOff()) {
            sendToLeftPlayer(List.of(answerToLeftPlayer));


            try {
                leftPlayerAction = readLeftPlayerAction(ACTIONS_SEPARATOR, 1).get(0);
                System.out.println("left - " + leftPlayerAction);
                //Проверка того, что игроки прислали корректные ходы в контексте игры
                if (isInvalidActions(leftPlayerAction, leftPlayerAction)) {
                    return getFailedResultOfGame(new GameProviderException("Некорректнное действие: " + leftPlayerAction, leftPlayer.getName()));
                }
            } catch (GameProviderException e) {
                System.out.println(e.getMessage());
                return getFailedResultOfGame(e);
            }
            if (isWinLeftPlayerAction(leftPlayerAction)) {
                executeLeftPlayerAction(leftPlayerAction);
                break;
            }
            answerToRightPlayer = executeLeftPlayerAction(leftPlayerAction) + ACTIONS_SEPARATOR;
            sendResponseToLeftPlayer(answerToRightPlayer);
            System.out.println("left response - " + answerToRightPlayer);
            sendToRightPlayer(List.of(answerToRightPlayer));



            try {
                rightPlayerAction = readRightPlayerAction(ACTIONS_SEPARATOR, 1).get(0);
                System.out.println("right - " + rightPlayerAction);
                //Проверка того, что игроки прислали корректные ходы в контексте игры
                if (isInvalidActions(rightPlayerAction, rightPlayerAction)) {
                    return getFailedResultOfGame(new GameProviderException("Некорректнное действие: " + rightPlayerAction, rightPlayer.getName()));
                }
            } catch (GameProviderException e) {
                System.out.println(e.getMessage());
                return getFailedResultOfGame(e);
            }
            if (isWinRightPlayerAction(rightPlayerAction)) {
                executeRightPlayerAction(rightPlayerAction);
                break;
            }
            answerToLeftPlayer = executeRightPlayerAction(rightPlayerAction) + ACTIONS_SEPARATOR;
            sendResponseToRightPlayer(answerToLeftPlayer);
            System.out.println("right response - " + answerToLeftPlayer);


            if(isHanged(90)) {
                System.out.println("зациклено");
                return getFailedResultOfGame(new GameProviderException("Игра преодолела лимит по времени", "Игра"));
            }
            try {
                checkIsAliveProcesses(); // проверим не сломались ли процессы игры
            } catch (GameProviderException e) {
                //Чистка за собой
                System.out.println(e.getMessage());
                return getFailedResultOfGame(e);
            }
        }

        //Чистка за собой
        closeAndDeleteProcesses();


        return getFineGameResult();
    }

    private void sendResponseToRightPlayer(String playerAnswer) {
        if (playerAnswer.contains("-1")) {
            sendToRightPlayer(List.of("1" + ACTIONS_SEPARATOR));
        } else {
            sendToRightPlayer(List.of("0" + ACTIONS_SEPARATOR));
        }
    }


    private void sendResponseToLeftPlayer(String playerAnswer) {
        if (playerAnswer.contains("-1")) {
            sendToLeftPlayer(List.of("1" + ACTIONS_SEPARATOR));
        } else {
            sendToLeftPlayer(List.of("0" + ACTIONS_SEPARATOR));
        }
    }

    private void sendStartMessages() {
        final String start = game.getStart();
        System.out.println(start);
        sendToLeftPlayer(List.of(start + ACTIONS_SEPARATOR));
        sendToRightPlayer(List.of(start + ACTIONS_SEPARATOR));

    }
}
