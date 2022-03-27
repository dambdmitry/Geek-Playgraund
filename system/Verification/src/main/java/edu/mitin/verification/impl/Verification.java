package edu.mitin.verification.impl;

import edu.mitin.games.Game;
import edu.mitin.games.RandomizeGameProvider;
import edu.mitin.games.model.Player;
import edu.mitin.games.model.ResultOfGame;
import edu.mitin.games.provider.GameProviderAbs;
import edu.mitin.games.randomizeGame.RandomizeGame;
import edu.mitin.verification.VerificationService;
import edu.mitin.verification.model.GameRelation;
import edu.mitin.verification.model.PlayerSolution;
import edu.mitin.verification.model.VerificationResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Verification implements VerificationService {
    //todo тоже вынести и унифицировать названия файлов, тк для джавы и мб еще кого важно совпадение нейминга файла и класса
    static final String DEFAULT_FILE_NAME = "Main";

    public static void main(String[] args) {
        final PlayerSolution solution = new PlayerSolution("abc", "java", "import java.util.Scanner;\n" +
                "\n" +
                "public class Main{\n" +
                "    static Scanner scanner = new Scanner(System.in);\n" +
                "    public static void main(String[] args) {\n" +
                "\n" +
                "        //Запишите сюда первый ход\n" +
                "        String firstAction = \"50\";\n" +
                "        System.out.println(firstAction);\n" +
                "        System.out.flush();\n" +
                "        int left = 0;\n" +
                "        int right = 100;\n" +
                "        int myShot = (right + left) / 2;\n" +
                "        while (true) {\n" +
                "            int answer = Integer.parseInt(scanner.nextLine());\n" +
                "            String response = \"\";\n" +
                "            //Write code\n" +
                "\n" +
                "            //Загаданное число меньше выстрела\n" +
                "            if (answer == -1) {\n" +
                "                left = myShot;\n" +
                "                myShot = (right+left)/2;\n" +
                "                response = String.valueOf(myShot);\n" +
                "            } else {\n" +
                "                right = myShot;\n" +
                "                myShot = (right+left)/2;\n" +
                "                response = String.valueOf(myShot);\n" +
                "            }\n" +
                "            System.out.println(response);\n" +
                "            System.out.flush();\n" +
                "        }\n" +
                "    }\n" +
                "}");
        new Verification().doVerification(solution, new GameRelation(new RandomizeGame(), RandomizeGameProvider.class));
    }

    @Override
    public VerificationResult doVerification(PlayerSolution solution, GameRelation game) {
        File solutionFile = new File(DEFAULT_FILE_NAME + getLanguageFileExtension(solution.getLanguage()));

        try (FileWriter fileWriter = new FileWriter(solutionFile)){
            fileWriter.write(solution.getCode());
            fileWriter.flush();
        } catch (IOException e) {
            return new VerificationResult(e.getMessage(), VerificationResult.Result.FAILURE);
        }
        Player player = new Player(solution.getPlayerName(), new String[]{getLanguageStartCommand(solution.getLanguage()), solutionFile.getAbsolutePath()});
        Player testPlayer = new Player("test", new String[]{getLanguageStartCommand(solution.getLanguage()), solutionFile.getAbsolutePath()});
        try {
            //todo колхоз жесть - пересмотреть архитектуру связи игра-провайдер!!!
            final GameProviderAbs gameProviderAbs = game.getProviderClass().getConstructor(Player.class, Player.class, Game.class).newInstance(player, testPlayer, game.getGame());
            final ResultOfGame resultOfGame = gameProviderAbs.executeGame();
            System.out.println(resultOfGame);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return new VerificationResult(e.getMessage(), VerificationResult.Result.FAILURE);
        }
        solutionFile.delete();
        return new VerificationResult("success", VerificationResult.Result.SUCCESS);
    }

    //todo вынести для общего пользования, мб сделать класс
    private String getLanguageStartCommand(String lang) {
        if (lang.equals("java")) {
            return "java ";
        }
        return null;
    }
    private String getLanguageFileExtension(String lang) {
        if (lang.equals("java")) {
            return ".java";
        }
        return null;
    }
}
