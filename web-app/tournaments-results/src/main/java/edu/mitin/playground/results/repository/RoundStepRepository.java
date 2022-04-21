package edu.mitin.playground.results.repository;

import edu.mitin.playground.results.entity.Round;
import edu.mitin.playground.results.entity.RoundStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundStepRepository extends JpaRepository<RoundStep, Long> {
    List<RoundStep> findAllByRound(Round round);
}
