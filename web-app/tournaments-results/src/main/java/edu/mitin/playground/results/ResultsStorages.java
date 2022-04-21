package edu.mitin.playground.results;

import edu.mitin.playground.results.entity.Round;
import edu.mitin.playground.results.entity.RoundStep;
import edu.mitin.playground.results.model.TournamentTableRow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResultsStorages {
    List<TournamentTableRow> createSortedTournamentTable(Long idTournament);

    List<Round> createUserRounds(Long tournamentId, String username);

    Round getRound(Long roundId);

    Boolean hasTournamentFailedRounds(Long tournamentId);

    List<Round> getFailedRounds(Long tournamentId);

    List<RoundStep> getSortedRoundSteps(Long roundId, String hostPlayerName, String guestPlayerName);

    boolean isAllGamesPlayed(Long tournamentId);
}
