package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
public class Player {
    private String id;
    private String name;
    private String username;
    private String password;
    private String[] exercises;
    private String[] offers;
    private String teamName;
    private String[][] skills;

    public Player(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;

    public Player() {
        super();
    }

    public Player(String name, String username, String password, String teamName, String[] invitations) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.teamName = teamName;
        this.invitations = invitations;
    }

    public Player(Player player) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getExercises() {
        return exercises;
    }

    public void setExercises(String[] exercises) {
        this.exercises = exercises;
    }

    public String[] getOffers() {
        return offers;
    }

    public void setOffers(String[] offers) {
        this.offers = offers;
    }

    public String[][] getSkills() {
        return skills;
    }

    public void setSkills(String[][] skills) {
        this.skills = skills;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
