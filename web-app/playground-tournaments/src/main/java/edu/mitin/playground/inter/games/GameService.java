package edu.mitin.playground.inter.games;

import edu.mitin.playground.inter.games.entity.Game;
import edu.mitin.playground.inter.games.entity.ProgramTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GameService {

    Optional<Game> getGameByName(String name);

    Optional<List<ProgramTemplate>> getGameProgramTemplates(String gameName);

    List<Game> getAllGames();
}
