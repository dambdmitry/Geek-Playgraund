package edu.mitin.verification.impl;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.games.service.model.Language;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.storage.TournamentStorage;
import edu.mitin.verification.VerificationService;
import edu.mitin.verification.dto.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Service
public class Verification implements VerificationService {
    //todo тоже вынести и унифицировать названия файлов, тк для джавы и мб еще кого важно совпадение нейминга файла и класса
    static final String DEFAULT_FILE_NAME = "Main";

    @Qualifier("tournamentStorageImpl")
    @Autowired
    private TournamentStorage storage;


    @Override
    public VerificationResult doVerification(Language language, String script, GameFactory.Game game) {
        File solutionFile = new File(DEFAULT_FILE_NAME + language.getFileExtension());

        try (FileWriter fileWriter = new FileWriter(solutionFile)){
            fileWriter.write(script);
            fileWriter.flush();
        } catch (IOException e) {
            return new VerificationResult(e.getMessage(), VerificationResult.Result.FAILURE);
        }
        Player testPlayer = new Player("test", new String[]{language.getCommandToExecution(), solutionFile.getAbsolutePath()});
        final ResultOfGame verification = Objects.requireNonNull(GameFactory.createGameProvider(testPlayer, testPlayer, game)).executeGame();
        System.out.println(verification);
        solutionFile.delete();
        if (verification.getResult().equals(ResultOfGame.Result.DONE)) {
            return new VerificationResult("success", VerificationResult.Result.SUCCESS);
        } else {
            return new VerificationResult(verification.getFailure().getDescription(), VerificationResult.Result.FAILURE);
        }
    }

    @Override
    public void savePlayerSolution(Long tournamentId, String playerName, String code, Language language) {
        storage.saveSolution(tournamentId, playerName, code, language.name());
    }
}
