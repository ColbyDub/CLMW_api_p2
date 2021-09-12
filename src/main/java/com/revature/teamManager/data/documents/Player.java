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
    private List<String> offers;
    private List<String> exercises;
    private String[][] skills;

    public Player(){
        super();
        this.offers = new ArrayList<>();
        this.exercises = new ArrayList<>();
        //Max of 5 skills can be changed later
        this.skills = new String[5][2];
    }

    //Only used in unit tests
    public Player( String name, String username, String password){
        super();
        this.name = name;
        this.username = username;
        this.password = password;
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

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }

    public String[][] getSkills() {
        return skills;
    }

    public void setSkills(String[][] skills) {
        this.skills = skills;
    }

    public List<String> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
