package edu.mitin.games.provider;

import edu.mitin.games.Game;
import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;
import edu.mitin.games.provider.exceptions.GameProviderException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Абстрактный класс, который предоставляет функционал для управления процессами играющих программ.
 */
public abstract class GameProviderAbs {
    //Игра в которую играют процессы
    protected Game game;

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


    public GameProviderAbs(Player leftPlayer, Player rightPlayer, Game game) {
        this.game = game;
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }

    //Метод исполнения игры, должен содеражать цикл игры.
    public abstract ResultOfGame executeGame();


    protected void initGame() throws GameProviderException {
        if (game == null || leftPlayer == null || rightPlayer == null) {
            throw new GameProviderException("Первичные параметры не заданы");
        }
        game.registerPlayers(leftPlayer, rightPlayer);
        boolean isFilesCreatedSuccessfully = createGameDirectoryAndFiles();
        if (!isFilesCreatedSuccessfully) {
            throw new GameProviderException("Файлы игры не создались");
        }
        createProgramProcesses();
        createGameStreams();
    }

    //Разделение на 2 метода чтобы не давать доступа к файлам детишкам
    protected List<String> readLeftPlayerAction(String actionsSeparator, int numberOfActionsPerStep) {
        String playerActions = readPlayerFile(leftPlayerGameFile);
        return getActionsFromWholeText(playerActions, actionsSeparator, numberOfActionsPerStep);
    }

    protected List<String> readRightPlayerAction(String actionsSeparator, int numberOfActionsPerStep) {
        String playerActions = readPlayerFile(rightPlayerGameFile);
        return getActionsFromWholeText(playerActions, actionsSeparator, numberOfActionsPerStep);
    }

    protected void sendToLeftPlayer(List<String> messages) {
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

    protected void sendToRightPlayer(List<String> messages) {
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
        //todo переделать по приличному - сделать свой эксепшн и прокинуть его
        String processError = "";
        if (!leftPlayerProcess.isAlive()) {
            while (leftPlayerErrorsReader.hasNextLine()) {
                processError += leftPlayerErrorsReader.nextLine() + "\n";
            }
            String message = "Программа игрока " + leftPlayer.getName() + " выдала ошибку:\n" + processError;
            throw new GameProviderException(message);
        }
        if (!rightPlayerProcess.isAlive()) {
            while (rightPlayerErrorsReader.hasNextLine()) {
                processError += rightPlayerErrorsReader.nextLine();
            }
            String message = "Программа игрока " + rightPlayer.getName() + " выдала ошибку:\n" + processError;
            throw new GameProviderException(message);
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

    private List<String> getActionsFromWholeText(String text, String actionsSeparator, int numberOfActionsPerStep) {
        String[] allActions = text.split(actionsSeparator);
        //todo для отладки - потом сразу заретурнить
        List<String> actions = Arrays.stream(allActions).skip(allActions.length - numberOfActionsPerStep).collect(Collectors.toList());
        return actions;
    }

    private boolean createGameDirectoryAndFiles() {
        //Создание директории для файлов игры
        String currentDirectoryPath = System.getProperty("user.dir");
        gameDirectory = new File(currentDirectoryPath + "\\" + leftPlayer.getName() + "vs" + rightPlayer.getName());
        boolean isDirectoryCreated = gameDirectory.mkdir();
        //Создание файлов игры
        leftPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + leftPlayer.getName() + ".txt");
        rightPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + rightPlayer.getName() + ".txt");
        boolean isGameFilesCreated = false;
        try {
            isGameFilesCreated = leftPlayerGameFile.createNewFile() && rightPlayerGameFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
        return isGameFilesCreated && isDirectoryCreated;
    }

    private void createProgramProcesses() throws GameProviderException {
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
