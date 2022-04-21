package edu.mitin.rest;

import edu.mitin.games.service.model.Response;
import edu.mitin.verification.dto.Solution;
import edu.mitin.verification.VerificationService;
import edu.mitin.verification.dto.VerificationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verification")
public class VerificationController {

    private VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping(value = "/verify-solution", produces = "application/json", consumes = "application/json")
    public Response verification(@RequestBody Solution solution) {
        final VerificationResult verificationResult = verificationService.verificationAndSaveSolution(solution);
        return new Response(verificationResult.getResult().equals(VerificationResult.Result.SUCCESS), verificationResult.getDescription());
    }
}
