package edu.mitin.storage;

import edu.mitin.storage.entity.Game;
import edu.mitin.storage.entity.Solution;
import edu.mitin.storage.entity.model.TournamentStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TournamentStorage {
    boolean saveSolution(Long tournamentId, String playerName, String code, String language);

    Optional<Game> getTournamentGame(Long tournamentId);

    List<Solution> getSolutionsByTournament(Long tournamentId);

    Long saveRound(Long tournamentId, String hostPlayerName, String guestPlayerName, String winnerName, String hostGoal, String guestGoal);

    Long saveDrawRound(Long tournamentId, String hostPlayerName, String guestPlayerName, String hostGoal, String guestGoal);

    Long saveFailedRound(Long tournamentId, String leftPlayerName, String rightPlayerName, String author, String description, String leftPlayerGoal, String rightPlayerGoal);

    void saveRoundStep(Long roundId, String playerName, String step, Long stepNumber);

    Boolean isPresentTournament(Long tournamentId);

    void setTournamentStatus(Long tournamentId, TournamentStatus status);

}
