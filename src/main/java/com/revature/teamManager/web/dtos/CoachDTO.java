package com.revature.teamManager.web.dtos;

import com.revature.teamManager.data.documents.Coach;

import java.util.List;
import java.util.Objects;

public class CoachDTO {

    private String coachName;
    private String username;
    private String teamName;
    private String sport;
    private List<String[]> players;

    public CoachDTO() {
        super();
    }

    public CoachDTO(Coach coach) {
        this.coachName = coach.getCoachName();
        this.username = coach.getUsername();
        this.teamName = coach.getTeamName();
        this.sport = coach.getSport();
        this.players = coach.getPlayers();
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
        CoachDTO coachDTO = (CoachDTO) o;
        return Objects.equals(coachName, coachDTO.coachName) && Objects.equals(username, coachDTO.username) && Objects.equals(teamName, coachDTO.teamName) && Objects.equals(sport, coachDTO.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coachName, username, teamName, sport);
    }

    @Override
    public String toString() {
        return "CoachDTO{" +
                "coachName='" + coachName + '\'' +
                ", username='" + username + '\'' +
                ", teamName='" + teamName + '\'' +
                ", sport='" + sport + '\'' +
                '}';
    }
}
