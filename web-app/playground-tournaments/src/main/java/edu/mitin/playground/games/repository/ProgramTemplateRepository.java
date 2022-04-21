package edu.mitin.playground.games.repository;

import edu.mitin.playground.games.entity.Game;
import edu.mitin.playground.games.entity.ProgramTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramTemplateRepository extends JpaRepository<ProgramTemplate, Long> {
    List<ProgramTemplate> findProgramTemplatesByGame(Game game);
}
