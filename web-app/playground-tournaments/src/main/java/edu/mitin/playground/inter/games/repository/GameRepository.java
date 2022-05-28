package edu.mitin.playground.inter.games.repository;

import edu.mitin.playground.inter.games.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findGameByName(String name);
}
