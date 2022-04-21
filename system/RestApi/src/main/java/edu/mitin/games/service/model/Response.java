package edu.mitin.games.service.model;

public class Response {
    private Boolean success;
    private String description;

    public Response(Boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }
}
