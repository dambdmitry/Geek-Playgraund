package edu.mitin.performance.impl;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.games.service.model.Failure;
import edu.mitin.games.service.model.Language;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.games.service.provider.GameProvider;
import edu.mitin.performance.PerformanceService;
import edu.mitin.performance.factory.CompilersCommandFactory;
import edu.mitin.performance.model.*;
import edu.mitin.storage.TournamentStorage;
import edu.mitin.storage.entity.Solution;
import edu.mitin.storage.entity.model.TournamentStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final TournamentStorage storage;


    public PerformanceServiceImpl(@Qualifier("tournamentStorageImpl") TournamentStorage storage) {
        this.storage = storage;
    }

    @Override
    public void executeTournament(Long tournamentId) {
        String gameName = getTournamentGame(tournamentId);
        List<Solution> tournamentSolutions = createTournamentSolutions(tournamentId);
        List<PlayerModel> players = createPlayersModels(tournamentSolutions);
        GamesGrid tournamentGamesGrid = createTournamentGrid(players);
        holdTournament(tournamentGamesGrid, gameName, tournamentId);
//        deletePlayersProgramFiles(players);
    }

    private String getTournamentGame(Long tournamentId) {
        return storage.getTournamentGame(tournamentId).get().getName();
    }

    private List<PlayerModel> createPlayersModels(List<Solution> solutions) {
        List<PlayerModel> players = new LinkedList<>();
        for (Solution solution : solutions) {
            PlayerModel player = new PlayerModel(solution.getPlayerName());
            Language language = Language.valueOf(solution.getLanguage());
            player.setCode(solution.getCode());
            player.setLanguage(language);
            players.add(player);
        }
        return players;
    }

    private File createPlayerProgramFile(Solution solution) {
        File playerFile = new File(solution.getPlayerName() + Language.valueOf(solution.getLanguage()).getFileExtension());
        try(FileWriter fileWriter = new FileWriter(playerFile)) {
            fileWriter.write(solution.getCode());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerFile;
    }

    private synchronized File createPlayerProgramFile(String playerName, Language language, String code, List<File> programFiles) {
        File playerFile = new File(playerName + language.getFileExtension());
        int identifier = 0;
        while (playerFile.exists()) {
            playerFile = new File(playerName + identifier + language.getFileExtension());
            identifier++;
        }
        try(FileWriter fileWriter = new FileWriter(playerFile)) {
            fileWriter.write(code);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        programFiles.add(playerFile);
        if (language.isTwoStepExecution()) {
            try {
                Process start = new ProcessBuilder()
                        .command(CompilersCommandFactory.createCompileCommand(language, playerFile.getAbsolutePath()))
                        .start();
                start.waitFor();
                File executionFile = new File(playerFile.getName().split("\\.")[0] + ".exe");
                programFiles.add(executionFile);
                return executionFile;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return playerFile;
        }
    }

    private TournamentResult holdTournament(GamesGrid tournamentGamesGrid, String game, Long tournamentId) {
        final List<Pair> pairs = tournamentGamesGrid.getPairs();

        Thread games = new Thread(() -> {
            List<File> programFiles = new LinkedList<>();
            boolean isTournamentFailed = false;
            for (Pair pair : pairs) {
                System.out.println(pair.getLeftPlayer().getPlayerName() + " vs " + pair.getRightPlayer().getPlayerName());
                PlayerModel leftPlayer = pair.getLeftPlayer();
                PlayerModel rightPlayer = pair.getRightPlayer();

                File leftPlayerFile = createPlayerProgramFile(leftPlayer.getPlayerName(), leftPlayer.getLanguage(), leftPlayer.getCode(), programFiles);
                File rightPlayerFile = createPlayerProgramFile(rightPlayer.getPlayerName(), rightPlayer.getLanguage(), rightPlayer.getCode(), programFiles);
                String[] leftPlayerCommand = CompilersCommandFactory.createExecuteCommand(leftPlayer.getLanguage(), leftPlayerFile.getAbsolutePath());
                String[] rightPlayerCommand = CompilersCommandFactory.createExecuteCommand(rightPlayer.getLanguage(), rightPlayerFile.getAbsolutePath());

                leftPlayer.setProgramFile(leftPlayerFile);
                rightPlayer.setProgramFile(rightPlayerFile);
                leftPlayer.setCommandToStart(leftPlayerCommand);
                rightPlayer.setCommandToStart(rightPlayerCommand);

                System.out.println(leftPlayerFile.getName());
                System.out.println(rightPlayerFile.getName());

                ResultOfGame resultOfFirstGame = holdGame(leftPlayer, rightPlayer, game);
                ResultOfGame resultOfSecondGame = holdGame(rightPlayer, leftPlayer, game);
                if (resultOfFirstGame.getResult() == ResultOfGame.Result.ERROR || resultOfSecondGame.getResult() == ResultOfGame.Result.ERROR) {
                    isTournamentFailed = true;
                }
                pair.setLeftPoints(resultOfFirstGame.getWinner().equals(pair.getLeftPlayer().getPlayerName()) ? 1 : 0);
                pair.setLeftPoints(resultOfFirstGame.getWinner().equals(pair.getRightPlayer().getPlayerName()) ? 1 : 0);
                saveResult(resultOfFirstGame, tournamentId);
                saveResult(resultOfSecondGame, tournamentId);
            }
            if (isTournamentFailed) {
                storage.setTournamentStatus(tournamentId, TournamentStatus.FAILURE);
            } else {
                storage.setTournamentStatus(tournamentId, TournamentStatus.CLOSED);
            }
            programFiles.forEach(f -> System.out.println(f.getName() + " " + f.delete()));
        });
        games.start();
        return null;
    }


    private List<Solution> createTournamentSolutions(Long tournamentId) {
        return storage.getSolutionsByTournament(tournamentId);
    }

    private GamesGrid createTournamentGrid(List<PlayerModel> players) {
        GamesGrid grid = new GamesGrid();
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                grid.addPair(new Pair(players.get(i), players.get(j)));
            }
        }
        return grid;
    }

    private ResultOfGame holdGame(PlayerModel leftPlayer, PlayerModel rightPlayer, String gameName) {
        final GameFactory.Game gameIdentifier = GameFactory.Game.valueOf(gameName);
        GameProvider game = GameFactory.createGameProvider(new Player(leftPlayer.getPlayerName(), leftPlayer.getCommandToStart()),
                new Player(rightPlayer.getPlayerName(), rightPlayer.getCommandToStart()),
                gameIdentifier);
        System.out.println(game);
        return game.executeGame();
    }

    private void saveResult(ResultOfGame result, Long tournamentId) {
        String leftPlayerName = result.getLeftPlayerName();
        String rightPlayerName = result.getRightPlayerName();
        if (result.getResult() == ResultOfGame.Result.ERROR) {
            Failure failure = result.getFailure();
            storage.saveFailedRound(tournamentId, leftPlayerName, rightPlayerName, failure.getAuthor(), failure.getDescription());
        } else {
            String winner = result.getWinner();
            Long roundId = storage.saveRound(tournamentId, leftPlayerName, rightPlayerName, winner, result.getLeftPlayerGoal(), result.getRightPlayerGoal());
            saveRoundSteps(leftPlayerName, roundId, result.getLeftPlayerSteps());
            saveRoundSteps(rightPlayerName, roundId, result.getRightPlayerSteps());
        }
    }

    private void saveRoundSteps(String playerName, Long roundId, List<String> playerSteps) {
        Long stepNumber = 0L;
        for (String step : playerSteps) {
            stepNumber++;
            storage.saveRoundStep(roundId, playerName, step, stepNumber);
        }
    }

}
