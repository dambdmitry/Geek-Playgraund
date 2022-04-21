package edu.mitin.games.service.factory;

import edu.mitin.games.randomizeGame.provider.RandomizeGameProvider;
import edu.mitin.games.service.model.Player;
import edu.mitin.games.service.provider.GameProvider;

public abstract class GameFactory {

    public enum Game{
        RANDOMIZE
    }

    public static GameProvider createGameProvider(Player leftPlayer, Player rightPlayer, Game game) {
        switch (game){
            case RANDOMIZE: return new RandomizeGameProvider(leftPlayer, rightPlayer);
            default: return null;
        }
    }
}
