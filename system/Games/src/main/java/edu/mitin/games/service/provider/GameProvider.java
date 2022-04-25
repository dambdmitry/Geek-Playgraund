package edu.mitin.games.service.provider;

import edu.mitin.games.service.Game;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.games.service.provider.exceptions.GameProviderException;

import java.io.*;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Абстрактный класс, который предоставляет функционал для управления процессами играющих программ.
 */
public abstract class GameProvider {
    //Игра в которую играют процессы
    private Game game;

    //Инстансы игроков, left и right - условные их названия.
    protected Player leftPlayer;
    protected Player rightPlayer;

    //Файлы ходов левого и правого игрока
    private File leftPlayerGameFile;
    private File rightPlayerGameFile;

    //Директория для файлов ходов
    private File gameDirectory;

    //Процессы исполнения игроков
    private Process leftPlayerProcess;
    private Process rightPlayerProcess;

    //Потоки для отправки сообщений процессам
    private BufferedWriter senderToLeftPlayer;
    private BufferedWriter senderToRightPlayer;

    //Потоки для чтения ошибок процессов
    private Scanner leftPlayerErrorsReader;
    private Scanner rightPlayerErrorsReader;


    private Long startTime;

    public GameProvider(Player leftPlayer, Player rightPlayer, Game game) {
        this.game = game;
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;

    }

    //Метод исполнения игры, должен содеражать цикл игры.
    public abstract ResultOfGame executeGame();


    protected ResultOfGame getFailedResultOfGame(GameProviderException ex) {
        closeAndDeleteProcesses();
        ResultOfGame resultOfGame = new ResultOfGame(ResultOfGame.Result.ERROR, ex.getAuthor(), ex.getMessage());
        resultOfGame.setLeftPlayerName(leftPlayer.getName());
        resultOfGame.setRightPlayerName(rightPlayer.getName());
        return resultOfGame;
    }


    protected void initGame() throws GameProviderException {
        if (game == null || leftPlayer == null || rightPlayer == null) {
            throw new GameProviderException("Первичные параметры не заданы", "ФС");
        }
        game.registerPlayers(leftPlayer.getName(), rightPlayer.getName());
        boolean isFilesCreatedSuccessfully = createGameDirectoryAndFiles();
        if (!isFilesCreatedSuccessfully) {
            throw new GameProviderException("Файлы игры не создались", "ФС");
        }
        createProgramProcesses();
        createGameStreams();
        startTime = System.currentTimeMillis();
    }

    //Разделение на 2 метода чтобы не давать доступа к файлам детишкам
    protected List<String> readLeftPlayerAction(String actionsSeparator, int numberOfActionsPerStep) throws GameProviderException {
        String playerActions = readPlayerFile(leftPlayerGameFile);
        try {
            return getActionsFromWholeText(playerActions, actionsSeparator, numberOfActionsPerStep);
        } catch (GameProviderException e) {
            e.setAuthor(leftPlayer.getName());
            throw e;
        }
    }

    protected List<String> readRightPlayerAction(String actionsSeparator, int numberOfActionsPerStep) throws GameProviderException {
        String playerActions = readPlayerFile(rightPlayerGameFile);
        try {
            return getActionsFromWholeText(playerActions, actionsSeparator, numberOfActionsPerStep);
        } catch (GameProviderException e) {
            e.setAuthor(rightPlayer.getName());
            throw e;
        }
    }

    protected synchronized void sendToLeftPlayer(List<String> messages) {
        try {
            for (String message : messages) {
                senderToLeftPlayer.write(message);
                senderToLeftPlayer.flush();
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void sendToRightPlayer(List<String> messages) {
        try {
            for (String message : messages) {
                senderToRightPlayer.write(message);
                senderToRightPlayer.flush();
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void checkIsAliveProcesses() throws GameProviderException {
        String processError = "";
        if (!leftPlayerProcess.isAlive()) {
            while (leftPlayerErrorsReader.hasNextLine()) {
                processError += leftPlayerErrorsReader.nextLine() + "\n";
            }
            String message = "Программа игрока " + leftPlayer.getName() + " выдала ошибку:\n" + processError;
            closeAndDeleteProcesses();
            throw new GameProviderException(message, leftPlayer.getName());
        }
        if (!rightPlayerProcess.isAlive()) {
            while (rightPlayerErrorsReader.hasNextLine()) {
                processError += rightPlayerErrorsReader.nextLine();
            }
            closeAndDeleteProcesses();
            String message = "Программа игрока " + rightPlayer.getName() + " выдала ошибку:\n" + processError;
            throw new GameProviderException(message, rightPlayer.getName());
        }
    }

    protected boolean closeAndDeleteProcesses() {
        leftPlayerProcess.destroyForcibly();
        rightPlayerProcess.destroyForcibly();
        try {
            leftPlayerProcess.waitFor();
            rightPlayerProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return leftPlayerGameFile.delete() && rightPlayerGameFile.delete() && gameDirectory.delete();
    }

    protected boolean isHanged(Integer seconds) {
        long milliSeconds = seconds * 1000;
        return System.currentTimeMillis() - startTime > milliSeconds;
    }

    protected boolean isInvalidActions(String leftPlayerAction, String rightPlayerAction) {
        return !game.isCorrectAction(leftPlayerAction) || !game.isCorrectAction(rightPlayerAction);
    }

    protected ResultOfGame getInvalidActionResult(String leftPlayerAction, String rightPlayerAction) {
        String author = !game.isCorrectAction(leftPlayerAction) ? leftPlayer.getName() : rightPlayer.getName();
        String action = !game.isCorrectAction(leftPlayerAction) ? leftPlayerAction : rightPlayerAction;
        final String message = "Некорректное действие " + action;
        System.out.println(message);
        return getFailedResultOfGame(new GameProviderException(message, author));
    }

    protected ResultOfGame getFineGameResult() {
        return game.getResultOfGame();
    }

    protected boolean hasWinAction(String leftPlayerAction, String rightPlayerAction) {
        return game.isWinLeftPlayerAction(leftPlayerAction) || game.isWinRightPlayerAction(rightPlayerAction);
    }

    protected String executeLeftPlayerAction(String action) {
        return game.executeLeftPlayerAction(action);
    }

    protected String executeRightPlayerAction(String action) {
        return game.executeRightPlayerAction(action);
    }

    private String readPlayerFile(File playerFile) {
        StringBuilder fileContent = new StringBuilder();
        int eof = -1;
        try (FileInputStream leftPlayerInput = new FileInputStream(playerFile)) {
            int i = -1;
            while ((i = leftPlayerInput.read()) != eof) {
                fileContent.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    private List<String> getActionsFromWholeText(String text, String actionsSeparator, int numberOfActionsPerStep) throws GameProviderException {
        String[] allActions = text.split(actionsSeparator);
        //todo для отладки - потом сразу заретурнить
        if (allActions.length < numberOfActionsPerStep) {
            throw new GameProviderException("Ход не определен");
        }
        List<String> actions = Arrays.stream(allActions).skip(allActions.length - numberOfActionsPerStep).collect(Collectors.toList());
        return actions;
    }

    private boolean createGameDirectoryAndFiles() {
        //Создание директории для файлов игры
        String currentDirectoryPath = System.getProperty("user.dir");
        gameDirectory = new File(currentDirectoryPath + "\\" + leftPlayer.getName() + "vs" + rightPlayer.getName());
        int identifier = 0;
        while (gameDirectory.exists()) {
            gameDirectory = new File(currentDirectoryPath + "\\" + leftPlayer.getName() + "vs" + rightPlayer.getName() + identifier);
            identifier++;
        }
        boolean isDirectoryCreated = gameDirectory.mkdir();
        //Создание файлов игры
        identifier = 0;
        leftPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + leftPlayer.getName() + "l.txt");
        while (leftPlayerGameFile.exists()) {
            leftPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + leftPlayer.getName() + identifier + "l.txt");
            identifier++;
        }
        rightPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + rightPlayer.getName() + "r.txt");
        while (rightPlayerGameFile.exists()) {
            rightPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + rightPlayer.getName() + identifier + "r.txt");
            identifier++;
        }
        boolean isGameFilesCreated = false;
        try {
            isGameFilesCreated = leftPlayerGameFile.createNewFile() && rightPlayerGameFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
        return isGameFilesCreated && isDirectoryCreated;
    }

    private synchronized void createProgramProcesses() throws GameProviderException {
        try {
            leftPlayerProcess = new ProcessBuilder()
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(leftPlayerGameFile))
                    .command(leftPlayer.getCommandToStartProgram())
                    .start();
            rightPlayerProcess = new ProcessBuilder()
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(rightPlayerGameFile))
                    .command(rightPlayer.getCommandToStartProgram())
                    .start();
            leftPlayerProcess.waitFor(1000, TimeUnit.MILLISECONDS);
            Thread.sleep(100);
        } catch (IOException | InterruptedException e) {
            throw new GameProviderException(e.getMessage(), e);
        }
    }

    private void createGameStreams() {
        //Для передачи ответов процессам (программам)
        senderToLeftPlayer = new BufferedWriter(new OutputStreamWriter(leftPlayerProcess.getOutputStream()));
        senderToRightPlayer = new BufferedWriter(new OutputStreamWriter(rightPlayerProcess.getOutputStream()));

        //Чтение ошибок
        leftPlayerErrorsReader = new Scanner(leftPlayerProcess.getErrorStream());
        rightPlayerErrorsReader = new Scanner(rightPlayerProcess.getErrorStream());
    }
}
