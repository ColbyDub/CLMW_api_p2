package com.revature.teamManager.web.dtos;

//this dto is used to add/delete sports and skills from a player's profile
public class AddToProfile {

    private String username;

    //this serves as a deleteValue variable if the skill/sport is to be deleted
    private String addedValue;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddedValue() {
        return addedValue;
    }

    public void setAddedValue(String addedValue) {
        this.addedValue = addedValue;
    }
}
