package edu.mitin.games.providers;

import edu.mitin.games.service.Game;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.games.service.provider.GameProvider;
import edu.mitin.games.service.provider.exceptions.GameProviderException;

import java.util.List;

/**
 * Провайдер для игр где игроки не знают о ходах своего соперника
 * у каждого своя цель, которую нужно достичь быстрее соперника
 * у игроков одновременно считываются их ходы и одновременно исполняются
 * кто за меньшее количеество ходов добьется цели игры - тот и победил
 */
public class IndependentGamesProvider extends GameProvider {

    public IndependentGamesProvider(Player leftPlayer, Player rightPlayer, Game game) {
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
        String actionsSeparator = "\r\n"; //разделитель ходов, которые игрок "пишет" в файл
        String leftPlayerAction = null;
        String rightPlayerAction = null;
        try {
            leftPlayerAction = readLeftPlayerAction(actionsSeparator, 1).get(0);
            rightPlayerAction = readRightPlayerAction(actionsSeparator, 1).get(0);
            //Проверка того, что игроки прислали корректные ходы в контексте игры
            if (isInvalidActions(leftPlayerAction, rightPlayerAction)) {
                return getInvalidActionResult(leftPlayerAction, rightPlayerAction);
            }
        } catch (GameProviderException e) {
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }

        System.out.println(leftPlayerAction + " " + rightPlayerAction);

        try {
            checkIsAliveProcesses(); // проверим не сломались ли процессы игры
        } catch (GameProviderException e) {
            //Чистка за собой
            System.out.println(e.getMessage());
            return getFailedResultOfGame(e);
        }


        //Цикл игры
        while (!hasWinAction(leftPlayerAction, rightPlayerAction)) {
            String answerToLeftPlayer = executeLeftPlayerAction(leftPlayerAction) + actionsSeparator;
            String answerToRightPlayer = executeRightPlayerAction(rightPlayerAction) + actionsSeparator;

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

            //Проверка того, что игроки прислали корректные ходы в контексте игры
            if (isInvalidActions(leftPlayerAction, rightPlayerAction)) {
                return getInvalidActionResult(leftPlayerAction, rightPlayerAction);
            }

            System.out.println(leftPlayerAction + "---" + rightPlayerAction);

            if(isHanged(90)) {
                System.out.println("зациклено");
                return getFailedResultOfGame(new GameProviderException("Игра преодолела лимит по времени", "Игра"));
            }
        }


        //Чистка за собой
        closeAndDeleteProcesses();

        return getFineGameResult();
    }

}
