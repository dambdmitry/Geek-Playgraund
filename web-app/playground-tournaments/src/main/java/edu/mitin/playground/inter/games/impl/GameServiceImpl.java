package edu.mitin.playground.inter.games.impl;

import edu.mitin.playground.inter.games.GameService;
import edu.mitin.playground.inter.games.entity.Game;
import edu.mitin.playground.inter.games.entity.ProgramTemplate;
import edu.mitin.playground.inter.games.repository.GameRepository;
import edu.mitin.playground.inter.games.repository.ProgramTemplateRepository;
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
