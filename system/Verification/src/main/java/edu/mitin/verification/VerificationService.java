package edu.mitin.verification;

import edu.mitin.games.service.factory.GameFactory;
import edu.mitin.games.service.model.Language;
import edu.mitin.verification.dto.Solution;
import edu.mitin.verification.dto.VerificationResult;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService {
    VerificationResult doVerification(Language language, String script, GameFactory.Game game);
    void savePlayerSolution(Long tournamentId, String playerName, String code, Language language);

    default VerificationResult verificationAndSaveSolution(Solution solution) {
        var verificationResult = doVerification(solution.getLanguage(), solution.getCode(), GameFactory.Game.valueOf(solution.getGame()));
        if (verificationResult.getResult().equals(VerificationResult.Result.SUCCESS)) {
            savePlayerSolution(solution.getTournamentId(), solution.getPlayerName(), solution.getCode(), solution.getLanguage());
        } else {
            verificationResult.setDescription(verificationResult.getDescription() + "\n Ваше решение не записано. Повторите попытку :)");
        }
        return verificationResult;
    }
}
