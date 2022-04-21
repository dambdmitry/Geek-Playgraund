package edu.mitin.playground.tournaments;

import edu.mitin.playground.tournaments.entity.Tournament;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TournamentService {

    /***
     * Сохранение турнира в хрранилище
     * @param tournament турнир для сохранения
     * @return id сохраненного турнира
     */
    Long registerTournament(Tournament tournament);

    boolean registerPlayerToTournament(Tournament tournament, User user);

    Optional<Tournament> getTournamentById(Long id);

    Optional<Tournament> findTournamentByName(String tournamentName);

    List<Tournament> getTournamentsByOwner(String ownerUsername);

    List<Player> getPlayersByTournament(Tournament tournament);

    List<Tournament> getTournamentsByPlayer(Player player);

    Iterable<Tournament> getByTournamentName(String tournamentName);

    List<Tournament> getAllTournaments();

    List<Tournament> getByPartTournamentName(String partName);

    List<User> getAccountsByTournament(Long tournamentId);
}
