package com.revature.teamManager.web.dtos;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerDTO {

    private String name;
    private String username;
    private List<String> offers;
    private List<String> exercises;
    private List<String> completedExercises;
    private List<Skills> skills = new ArrayList<Skills>();

    public PlayerDTO() {
        super();
    }

    public PlayerDTO(Player player) {
        super();
        this.name = player.getName();
        this.username = player.getUsername();
        this.offers = player.getOffers();
        this.exercises = player.getExercises();
        this.completedExercises = player.getCompletedExercises();
        this.skills = player.getSkills();
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

    public List<String> getCompletedExercises() {
        return exercises;
    }

    public void setCompletedExercises(List<String> completedExercises) {
        this.completedExercises = completedExercises;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(name, playerDTO.name) && Objects.equals(username, playerDTO.username) && Objects.equals(offers, playerDTO.offers) && Objects.equals(exercises, playerDTO.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, offers, exercises);
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", offers=" + offers +
                ", exercises=" + exercises +
                '}';
    }
}
