package com.revature.teamManager.data.documents;

import java.util.Arrays;
import java.util.Objects;

public class Coach {
    private String coachName;
    private String username;
    private String password;
    private String teamName;
    private String sport;
    private String[][] players;

    public Coach() {
        super();
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
        return Objects.equals(coachName, coach.coachName) && Objects.equals(username, coach.username) && Objects.equals(password, coach.password) && Objects.equals(teamName, coach.teamName) && Objects.equals(sport, coach.sport) && Arrays.equals(players, coach.players);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(coachName, username, password, teamName, sport);
        result = 31 * result + Arrays.hashCode(players);
        return result;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachName='" + coachName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", teamName='" + teamName + '\'' +
                ", sport='" + sport + '\'' +
                ", players=" + Arrays.toString(players) +
                '}';
    }
}
