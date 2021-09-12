package com.revature.teamManager.web.dtos;

import com.revature.teamManager.data.documents.Player;
import java.util.List;
import java.util.Objects;

public class PlayerDTO {

    private String name;
    private String username;
    private List<String> offers;
    private List<String> exercises;
    private String[][] skills;

    public PlayerDTO() {
        super();
    }

    public PlayerDTO(Player player) {
        super();
        this.name = player.getName();
        this.username = player.getUsername();
        this.offers = player.getOffers();
        this.exercises = player.getExercises();
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

    public String[][] getSkills() {
        return skills;
    }

    public void setSkills(String[][] skills) {
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
