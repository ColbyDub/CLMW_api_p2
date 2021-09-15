package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "players")
public class Player {
    private String id;
    private String name;
    private String username;
    private String password;
    private String teamName;
    private String[] invitations;
    private List<String> offers = new ArrayList<String>();
    private List<String> exercises = new ArrayList<String>();
    private List<String> sports = new ArrayList<String>();
    private List<Skills> skills = new ArrayList<Skills>();

    public Player(){
    }

    //Only used in unit tests
    public Player( String name, String username, String password, String sport) {
        super();
        this.name = name;
        this.username = username;
        this.password = password;
        this.sports.add(sport);
    }

    public Player(String name, String username, String password, String sport, String teamName, List<String> offers) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.sports.add(sport);
        this.teamName = teamName;
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", offers=" + offers +
                ", exercises=" + exercises +
                ", sports=" + sports +
                ", skills=" + skills +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public String[] getInvitations() {
        return invitations;
    }

    public void setInvitations(String[] invitations) {
        this.invitations = invitations;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }
}
