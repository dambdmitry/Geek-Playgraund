package edu.mitin.games.provider.exceptions;

public class GameProviderException extends Exception{
    public GameProviderException(String message) {
        super(message);
    }

    public GameProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
