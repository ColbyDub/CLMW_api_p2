package com.revature.teamManager.web.dtos;

//this dto is used to set an exercise as complete or incomplete
public class ModifyExercise {
    private String playerUsername;
    private String exercise;

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }
}

