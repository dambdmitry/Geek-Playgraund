package edu.mitin.playground.inter.tournaments;

import edu.mitin.playground.inter.tournaments.entity.Solution;
import edu.mitin.playground.inter.tournaments.entity.Tournament;
import edu.mitin.playground.inter.tournaments.entity.model.TournamentStatus;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TournamentService {

    Long registerTournament(Tournament tournament);

    void setTournamentStatus(Long tournamentId, TournamentStatus status);

    boolean registerPlayerToTournament(Tournament tournament, User user);

    Optional<Tournament> getTournamentById(Long id);

    Optional<Tournament> findTournamentByName(String tournamentName);

    List<Tournament> getTournamentsByOwner(String ownerUsername);

    List<Player> getPlayersByTournament(Tournament tournament);

    List<Tournament> getTournamentsByPlayer(Player player);

    Player getPlayerByUserName(String playerName);

    List<Tournament> getAllTournaments();

    List<Tournament> getByPartTournamentName(String partName);

    List<User> getAccountsByTournament(Long tournamentId);

    void savePlayerTournamentPoints(Long playerId, Long tournamentId, int points);

    List<Player> getTopPlayers();

    List<Solution> getTournamentSolutions(Long tournamentId);

    void clearPlayersPoints(Long tournamentId);
}
