package com.revature.teamManager.web.dtos;

//this dto is used to assign a position to a player on a team
public class AssignPositionRequest {

    private String coachUsername;
    private String playerUsername;
    private String position;

    public AssignPositionRequest() {
        super();
    }

    public String getCoachUsername() {
        return coachUsername;
    }

    public void setCoachUsername(String coachUsername) {
        this.coachUsername = coachUsername;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
