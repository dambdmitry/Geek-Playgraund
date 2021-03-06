package edu.mitin.verification.impl;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.model.ResultOfGame;
import edu.mitin.performance.factory.Compiler;
import edu.mitin.performance.factory.CompilerFactory;
import edu.mitin.storage.TournamentStorage;
import edu.mitin.verification.VerificationService;
import edu.mitin.verification.dto.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Service
public class Verification implements VerificationService {
    static final String DEFAULT_FILE_NAME = "Main";

    @Qualifier("tournamentStorageImpl")
    @Autowired
    private TournamentStorage storage;

    @Autowired
    private CompilerFactory factory;


    @Override
    public VerificationResult doVerification(String language, String script, GameFactory.Game game) {
        List<File> files = new LinkedList<>();
        File executionFile = null;
        Compiler compiler;
        try {
            compiler = factory.getCompiler(language);
        } catch (Exception exception) {
            return new VerificationResult("Нет такого компилятора", VerificationResult.Result.FAILURE);
        }
        try {
            executionFile = createExecutionFile(compiler, script, files);
        } catch (Exception e) {
            return new VerificationResult(e.getMessage(), VerificationResult.Result.FAILURE);
        }
        String[] command = compiler.getExecutionCommand(executionFile.getAbsolutePath());
        Player testPlayer = new Player("test", command);
        final ResultOfGame verification = Objects.requireNonNull(GameFactory.createGameProvider(testPlayer, testPlayer, game)).executeGame();
        System.out.println(verification);
        files.forEach(File::delete);
        if (verification.getResult().equals(ResultOfGame.Result.DONE)) {
            return new VerificationResult("success", VerificationResult.Result.SUCCESS);
        } else {
            return new VerificationResult(verification.getFailure().getDescription(), VerificationResult.Result.FAILURE);
        }
    }

    private File createExecutionFile(Compiler compiler, String script, List<File> files) throws Exception {
        File solutionFile = new File(DEFAULT_FILE_NAME + compiler.getFileExtension());
        try (FileWriter fileWriter = new FileWriter(solutionFile)){
            fileWriter.write(script);
            fileWriter.flush();
        } catch (IOException e) {
            throw new Exception(e);
        }
        files.add(solutionFile);
        if (compiler.isTwoStepCompileLanguage()) {
            try {
                String inputFilePath = solutionFile.getAbsolutePath();
                String outputFilePath = solutionFile.getAbsolutePath().split("\\.")[0];
                Process compileProcess = new ProcessBuilder()
                        .command(compiler.getCompileCommand(inputFilePath, outputFilePath))
                        .start();
                compileProcess.waitFor(10, TimeUnit.MILLISECONDS);
                checkCompileErrors(compileProcess.getErrorStream());
                compileProcess.waitFor();
                File executionFile = new File(outputFilePath + ".exe");
                files.add(executionFile);
                return executionFile;
            } catch (IOException | InterruptedException e) {
                throw new Exception(e);
            }
        } else {
            return solutionFile;
        }
    }

    private void checkCompileErrors(InputStream processErrorIS) throws Exception {
        Scanner errorReader = new Scanner(processErrorIS);
        if (errorReader.hasNext()) {
            String error = "";
            while (errorReader.hasNextLine()) {
                error += errorReader.nextLine();
            }
            throw new Exception(error);
        }
    }

    @Override
    public void savePlayerSolution(Long tournamentId, String playerName, String code, String language) {
        storage.saveSolution(tournamentId, playerName, code, language);
    }
}
