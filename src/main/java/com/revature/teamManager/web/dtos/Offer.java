package com.revature.teamManager.web.dtos;

//this dto is used to extend an offer to a player to join a coaches team
public class Offer {
    private String coachUsername;
    private String playerUsername;

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
}
