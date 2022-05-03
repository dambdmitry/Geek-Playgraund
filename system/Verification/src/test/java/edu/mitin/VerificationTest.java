package edu.mitin;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.verification.dto.VerificationResult;
import edu.mitin.verification.impl.Verification;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VerificationTest {

    private String successSolutionCode;
    private String failureSolutionCode;

    private Verification service = new Verification();

    @Before
    public void initCode() {
        successSolutionCode = readFromResources("successJavaCode.txt");
        failureSolutionCode = readFromResources("failureJavaCode.txt");
    }

    private String readFromResources(String resourceName) {
        try {
            return Files.lines(Paths.get("src","test", "resources", resourceName)).collect(Collectors.joining("\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void testSuccessVerification(){
        final VerificationResult verificationResult = service.doVerification("JAVA", successSolutionCode, GameFactory.Game.RANDOMIZE);
        assertEquals(VerificationResult.Result.SUCCESS, verificationResult.getResult());
    }

    @Test
    public void testFailureVerification(){
        final VerificationResult verificationResult = service.doVerification("JAVA", failureSolutionCode, GameFactory.Game.RANDOMIZE);
        assertEquals(VerificationResult.Result.FAILURE, verificationResult.getResult());
    }
}
