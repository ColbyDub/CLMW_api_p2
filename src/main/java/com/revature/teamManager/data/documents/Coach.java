package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Document(collection = "teams")
public class Coach {
    private String id;
    private String coachName;
    private String username;
    private String password;
    private String teamName;
    private String sport;
    private List<String[]> players;

    public Coach() {
        super();
        this.players = new ArrayList<>();
    }

    public Coach(String id, String coachName, String username, String password, String teamName, String sport, List<String[]> players) {
        this.id = id;
        this.coachName = coachName;
        this.username = username;
        this.password = password;
        this.teamName = teamName;
        this.sport = sport;
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public List<String[]> getPlayers() {
        return players;
    }

    public void setPlayers(List<String[]> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(id, coach.id) && Objects.equals(coachName, coach.coachName) && Objects.equals(username, coach.username) && Objects.equals(password, coach.password) && Objects.equals(teamName, coach.teamName) && Objects.equals(sport, coach.sport) && Objects.equals(players, coach.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coachName, username, password, teamName, sport, players);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachName='" + coachName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", teamName='" + teamName + '\'' +
                ", sport='" + sport + '\'' +
                ", players=" + players +
                '}';
    }
}
