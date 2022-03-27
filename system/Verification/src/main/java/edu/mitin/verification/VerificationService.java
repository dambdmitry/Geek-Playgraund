package edu.mitin.verification;

import edu.mitin.games.Game;
import edu.mitin.games.provider.GameProviderAbs;
import edu.mitin.verification.model.GameRelation;
import edu.mitin.verification.model.PlayerSolution;
import edu.mitin.verification.model.VerificationResult;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService {

    VerificationResult doVerification(PlayerSolution solution, GameRelation game);
}
