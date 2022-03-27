package edu.mitin.verification.model;

import edu.mitin.games.Game;
import edu.mitin.games.provider.GameProviderAbs;

public class GameRelation {
    Game game;
    Class<? extends GameProviderAbs> providerClass;

    public GameRelation(Game game, Class<? extends GameProviderAbs> providerClass) {
        this.game = game;
        this.providerClass = providerClass;
    }

    public Game getGame() {
        return game;
    }

    public Class<? extends GameProviderAbs> getProviderClass() {
        return providerClass;
    }
}
