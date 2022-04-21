package edu.mitin.playground.games.repository;

import edu.mitin.playground.games.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findGameByName(String name);
}
