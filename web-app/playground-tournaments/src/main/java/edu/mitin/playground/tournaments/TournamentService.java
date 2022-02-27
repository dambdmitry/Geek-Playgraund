package edu.mitin.playground.tournaments;

import edu.mitin.playground.users.model.Player;
import org.springframework.stereotype.Service;
import edu.mitin.playground.tournaments.model.Tournament;

import java.util.List;
import java.util.Optional;

@Service
public interface TournamentService {

    boolean registerTournament(Tournament tournament);

    Optional<Tournament> getTournamentById(Long id);

    List<Tournament> getTournamentsByOwner(String ownerUsername);

    List<Player> getPlayersByTournament(Tournament tournament);

    List<Tournament> getTournamentsByPlayer(Player player);

    List<Tournament> getAllTournaments();
}
