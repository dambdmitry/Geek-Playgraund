package edu.mitin.playground.results.repository;

import edu.mitin.playground.results.entity.Round;
import edu.mitin.playground.tournaments.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Long> {
    List<Round> findAllByGuestNameAndTournament(String guestName, Tournament tournament);

    List<Round> findAllByHostNameAndTournament(String hostName, Tournament tournament);

    List<Round> findAllByTournamentAndFailureIsNotNull(Tournament tournament);

    List<Round> findAllByTournament(Tournament tournament);
}
