package edu.mitin.games.service.factory;

import edu.mitin.games.battleship.game.BattleShipGame;
import edu.mitin.games.lines.game.LinesGame;
import edu.mitin.games.providers.B2BProvider;
import edu.mitin.games.randomizeGame.game.RandomizeGame;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.provider.GameProvider;
import edu.mitin.games.providers.IndependentGamesProvider;
public abstract class GameFactory {

    public enum Game{
        RANDOMIZE,
        BATTLESHIP,
        LINES;
    }

    public static GameProvider createGameProvider(Player leftPlayer, Player rightPlayer, Game game) {
        switch (game){
            case RANDOMIZE: return new IndependentGamesProvider(leftPlayer, rightPlayer, new RandomizeGame(leftPlayer.getName(), rightPlayer.getName()));
            case BATTLESHIP: return new IndependentGamesProvider(leftPlayer, rightPlayer, new BattleShipGame(leftPlayer.getName(), rightPlayer.getName()));
            case LINES: return new B2BProvider(leftPlayer, rightPlayer, new LinesGame(leftPlayer.getName(), rightPlayer.getName()));
            default: return null;
        }
    }
}
