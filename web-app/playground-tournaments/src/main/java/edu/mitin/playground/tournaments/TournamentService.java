package edu.mitin.playground.tournaments;

import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.User;
import org.springframework.stereotype.Service;
import edu.mitin.playground.tournaments.model.Tournament;

import java.util.List;
import java.util.Optional;

@Service
public interface TournamentService {

    boolean registerTournament(Tournament tournament);

    boolean registerPlayerToTournament(Tournament tournament, User user);

    Optional<Tournament> getTournamentById(Long id);

    Optional<Tournament> findTournamentByName(String tournamentName);

    List<Tournament> getTournamentsByOwner(String ownerUsername);

    List<Player> getPlayersByTournament(Tournament tournament);

    List<Tournament> getTournamentsByPlayer(Player player);

    Iterable<Tournament> getByTournamentName(String tournamentName);

    List<Tournament> getAllTournaments();

    List<Tournament> getByPartTournamentName(String partName);
}
