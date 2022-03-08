package edu.mitin.playground.tournaments.repository;

import edu.mitin.playground.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.mitin.playground.tournaments.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findTournamentsByOwner(User owner);

    Optional<Tournament> findTournamentByName(String name);

    Iterable<Tournament> findTournamentsByName(String name);

    List<Tournament> findTournamentsByNameContaining(String name);
}
