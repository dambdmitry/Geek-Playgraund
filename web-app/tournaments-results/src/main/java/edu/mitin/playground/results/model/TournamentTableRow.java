package edu.mitin.playground.results.model;

public class TournamentTableRow {
    private Integer position;
    private String username;
    private Integer points;
    private Integer losesCount;
    private Integer winsCount;

    public TournamentTableRow(Integer position, String username, Integer points, Integer losesCount, Integer winsCount) {
        this.position = position;
        this.username = username;
        this.points = points;
        this.losesCount = losesCount;
        this.winsCount = winsCount;
    }

    public TournamentTableRow() {
    }

    public Integer getPosition() {
        return position;
    }

    public String getUsername() {
        return username;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getLosesCount() {
        return losesCount;
    }

    public Integer getWinsCount() {
        return winsCount;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setLosesCount(Integer losesCount) {
        this.losesCount = losesCount;
    }

    public void setWinsCount(Integer winsCount) {
        this.winsCount = winsCount;
    }
}
