package edu.mitin.performance.model;

public class Pair {
    private PlayerModel leftPlayer;
    private PlayerModel rightPlayer;

    private Integer leftPoints;
    private Integer rightPoints;

    public Pair(PlayerModel leftPlayer, PlayerModel rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        leftPoints = 0;
        rightPoints = 0;
    }

    public Integer getPlayerPoints(String playerName) {
        if (leftPlayer.getPlayerName().equals(playerName)) {
            return leftPoints;
        } else if (rightPlayer.getPlayerName().equals(playerName)) {
            return rightPoints;
        }
        else return 0;
    }

    public Integer getLeftPoints() {
        return leftPoints;
    }

    public void setLeftPoints(Integer leftPoints) {
        this.leftPoints = leftPoints;
    }

    public Integer getRightPoints() {
        return rightPoints;
    }

    public void setRightPoints(Integer rightPoints) {
        this.rightPoints = rightPoints;
    }

    public PlayerModel getLeftPlayer() {
        return leftPlayer;
    }

    public PlayerModel getRightPlayer() {
        return rightPlayer;
    }
}
