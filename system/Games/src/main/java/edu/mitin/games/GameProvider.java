package edu.mitin.games;

import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;
import edu.mitin.games.randomizeGame.RandomizeGame;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//Сделать абстрактный класс
public class GameProvider {

    //Игра в которую играют процессы
    private Game game;

    //Инстансы игроков, left и right - условные их названия. left - ХОДИТ ПЕРВЫМ!
    private Player leftPlayer;
    private Player rightPlayer;

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




    public static void main(String[] args) throws Exception {
        Player player1 = new Player("one", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution1.java"});
        Player player2 = new Player("two", new String[]{"java", "D:\\University\\Diploma\\Geek-Playgraund\\system\\testSoluttions\\randomizeGame\\solution2.java"});
        ResultOfGame resultOfGame = new GameProvider(player1, player2, new RandomizeGame()).executeGame();
        System.out.println();
        System.out.println(resultOfGame);

    }

    public GameProvider(Player leftPlayer, Player rightPlayer, Game game) {
        this.game = game;
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }



    public ResultOfGame executeGame() throws Exception {
        //Инициализация полей игры
        initGame();

        //ИГРА

        //Чтение ходов
        leftPlayerProcess.waitFor(1000, TimeUnit.MILLISECONDS);
        FileInputStream leftPlayerInput = new FileInputStream(leftPlayerGameFile);
        int i = -1;
        String leftPlayerAction = "";
        //
        while ((i=leftPlayerInput.read()) != 13) {
            leftPlayerAction += (char)i;
        }
        leftPlayerInput.close();

        FileInputStream rightPlayerInput = new FileInputStream(rightPlayerGameFile);
        String rightPlayerAction = "";
        while ((i=rightPlayerInput.read()) != 13) {
            rightPlayerAction += (char)i;
        }
        rightPlayerInput.close();

        System.out.println(leftPlayerAction + "---" + rightPlayerAction);


        //Провверка жизни процессов
        if (!leftPlayerProcess.isAlive()) {
            while (leftPlayerErrorsReader.hasNextLine()) {
                System.out.println(leftPlayerErrorsReader.nextLine());
            }
        }
        if (!rightPlayerProcess.isAlive()) {
            while (rightPlayerErrorsReader.hasNextLine()) {
                System.out.println(rightPlayerErrorsReader.nextLine());
            }
        }

        //Проверка
        if (!game.isCorrectAction(leftPlayerAction) || !game.isCorrectAction(rightPlayerAction)) {
            throw new Exception("Некорректное действие " + leftPlayerAction + " " + rightPlayerAction);
        }

        //Цикл игры
        while (!game.isWinAction(leftPlayerAction, leftPlayer) && !game.isWinAction(rightPlayerAction, rightPlayer)) {
            String answerToLeftPlayer = game.doAction(leftPlayerAction, leftPlayer) + "\r\n";
            String answerToRightPlayer = game.doAction(rightPlayerAction, rightPlayer) + "\r\n";

            System.out.println(answerToLeftPlayer + "!!!" + answerToRightPlayer);

            //Отправка ответов

            senderToLeftPlayer.write(answerToLeftPlayer);
            senderToLeftPlayer.flush();
            senderToRightPlayer.write(answerToRightPlayer);
            senderToRightPlayer.flush();



            Thread.sleep(100);


            //Чтение новых ходов

            leftPlayerInput = new FileInputStream(leftPlayerGameFile);
            leftPlayerAction = "";
            rightPlayerAction = "";
            while ((i=leftPlayerInput.read()) != -1) {
                leftPlayerAction += (char)i;
            }
            leftPlayerInput.close();
            String[] split = leftPlayerAction.split("\r\n");
            leftPlayerAction = split[split.length-1];

            rightPlayerInput = new FileInputStream(rightPlayerGameFile);
            while ((i=rightPlayerInput.read()) != -1) {
                rightPlayerAction += (char)i;
            }
            rightPlayerInput.close();
            split = rightPlayerAction.split("\r\n");
            rightPlayerAction = split[split.length-1];

            System.out.println(leftPlayerAction + "---" + rightPlayerAction);
        }


        //Чистка за собой
        leftPlayerProcess.destroyForcibly();
        rightPlayerProcess.destroyForcibly();
        leftPlayerProcess.waitFor();
        rightPlayerProcess.waitFor();

        leftPlayerGameFile.delete();
        rightPlayerGameFile.delete();
        gameDirectory.delete();
//        System.out.println(player1File.delete());
//        System.out.println(player2File.delete());

        if (game.isWinAction(leftPlayerAction, leftPlayer)) {
            System.out.println("winner is " + leftPlayer.getName());
        } else {
            System.out.println("winner is " + rightPlayer.getName());
        }
        return game.getResultOfGame();
    }

    private void initGame() throws IOException {
        if (game == null || leftPlayer == null || rightPlayer == null) {
            //todo сделать свой эксепшн
            throw new RuntimeException("Первичные параметры не заданы");
        }
        game.registerPlayers(leftPlayer, rightPlayer);

        //Создание директории для файлов игры
        String fileDirectoryPath = System.getProperty("user.dir");
        this.gameDirectory = new File(fileDirectoryPath + "\\" + leftPlayer.getName() + "vs" + rightPlayer.getName());
        gameDirectory.mkdir();
        //Создание файлов игры
        this.leftPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + leftPlayer.getName() + ".txt");
        this.rightPlayerGameFile = new File(gameDirectory.getAbsolutePath() + "\\" + rightPlayer.getName() + ".txt");
        this.leftPlayerGameFile.createNewFile();
        this.rightPlayerGameFile.createNewFile();

        //Создание процессов программ игроков
        this.leftPlayerProcess = new ProcessBuilder()
                .redirectOutput(ProcessBuilder.Redirect.appendTo(this.leftPlayerGameFile))
                .command(leftPlayer.getCommandToStartProgram())
                .start();
        this.rightPlayerProcess = new ProcessBuilder()
                .redirectOutput(ProcessBuilder.Redirect.appendTo(this.rightPlayerGameFile))
                .command(rightPlayer.getCommandToStartProgram())
                .start();



        //Для передачи ответов процессам (программам)
        this.senderToLeftPlayer = new BufferedWriter(new OutputStreamWriter(this.leftPlayerProcess.getOutputStream()));
        this.senderToRightPlayer = new BufferedWriter(new OutputStreamWriter(this.rightPlayerProcess.getOutputStream()));

        //Чтение ошибок
        this.leftPlayerErrorsReader = new Scanner(this.leftPlayerProcess.getErrorStream());
        this.rightPlayerErrorsReader = new Scanner(this.rightPlayerProcess.getErrorStream());
    }

}
