package edu.mitin.storage.entity.model;

public enum TournamentStatus {
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
