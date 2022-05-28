package edu.mitin.playground.results.model;

public class TournamentTableRow {
    private Integer position;
    private String username;
    private Integer points;
    private Integer losesCount;
    private Integer winsCount;
    private Integer drawCount;

    public TournamentTableRow() {
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
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
