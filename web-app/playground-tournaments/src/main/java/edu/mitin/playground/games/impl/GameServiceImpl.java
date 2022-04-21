package edu.mitin.playground.games.impl;

import edu.mitin.playground.games.GameService;
import edu.mitin.playground.games.entity.Game;
import edu.mitin.playground.games.entity.ProgramTemplate;
import edu.mitin.playground.games.repository.GameRepository;
import edu.mitin.playground.games.repository.ProgramTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ProgramTemplateRepository programTemplateRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, ProgramTemplateRepository programTemplateRepository) {
        this.gameRepository = gameRepository;
        this.programTemplateRepository = programTemplateRepository;
    }

    @Override
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public Optional<Game> getGameByName(String name) {
        return Optional.ofNullable(gameRepository.findGameByName(name));
    }

    @Override
    public Optional<List<ProgramTemplate>> getGameProgramTemplates(String gameName) {
        final Optional<Game> game = getGameByName(gameName);
        if (game.isPresent()) {
            final List<ProgramTemplate> programTemplatesByGame = programTemplateRepository.findProgramTemplatesByGame(game.get());
            return Optional.ofNullable(programTemplatesByGame);
        }
        return Optional.empty();
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
