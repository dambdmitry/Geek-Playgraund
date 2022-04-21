package edu.mitin.games.service.provider.exceptions;

public class GameProviderException extends Exception{

    private String author;

    public GameProviderException(String message, String author) {
        super(message);
        this.author = author;
    }

    public GameProviderException(String message) {
        super(message);
    }


    public GameProviderException(String message, Throwable cause, String author) {
        super(message, cause);
        this.author = author;
    }

    public GameProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
