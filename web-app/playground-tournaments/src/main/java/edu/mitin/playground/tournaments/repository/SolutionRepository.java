package edu.mitin.playground.tournaments.repository;

import edu.mitin.playground.tournaments.entity.Solution;
import edu.mitin.playground.tournaments.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findAllByTournament(Tournament tournament);
}
