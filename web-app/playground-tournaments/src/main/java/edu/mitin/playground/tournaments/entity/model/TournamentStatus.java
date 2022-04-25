package edu.mitin.playground.tournaments.entity.model;

public enum TournamentStatus {
    OPEN("Турнир открыт для регистрации"),
    IN_PROCESS("Сейчас проходят игры турнира"),
    CLOSED("Турнир завершен"),
    FAILURE("Турнир завершен с ошибкой");


    private String status;

    TournamentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
