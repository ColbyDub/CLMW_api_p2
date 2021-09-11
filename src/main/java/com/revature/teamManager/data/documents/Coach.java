package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Document(collection = "teams")
public class Coach {

    private String id;
    private String coachName;
    private String username;
    private String password;
    private String teamName;
    private String sport;
    private String[][] players;

    public Coach() {
        super();
        this.players = new String[0][];
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

    public String[][] getPlayers() {
        return players;
    }

    public void setPlayers(String[][] players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(id, coach.id) && Objects.equals(coachName, coach.coachName) && Objects.equals(username, coach.username) && Objects.equals(password, coach.password) && Objects.equals(teamName, coach.teamName) && Objects.equals(sport, coach.sport) && Arrays.equals(players, coach.players);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, coachName, username, password, teamName, sport);
        result = 31 * result + Arrays.hashCode(players);
        return result;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id='" + id + '\'' +
                ", coachName='" + coachName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", teamName='" + teamName + '\'' +
                ", sport='" + sport + '\'' +
                ", players=" + Arrays.toString(players) +
                '}';
    }
}
