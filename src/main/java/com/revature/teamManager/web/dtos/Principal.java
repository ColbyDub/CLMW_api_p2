package com.revature.teamManager.web.dtos;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Recruiter;
import io.jsonwebtoken.Claims;

import java.util.Objects;

public class Principal {

    private String id;      //Needed? ->or use username as unique id
    private String username;
    private String role;

    public Principal() {
        super();
    }

    public Principal(Coach subject) {
        this.username = subject.getUsername();
        this.role = "Coach";
    }

    public Principal(Player subject) {
        this.username = subject.getUsername();
        this.role = "Player";
    }

    public Principal(Recruiter subject) {
        this.username = subject.getUsername();
        this.role = "Recruiter";
    }

    public Principal(Claims jwtClaims) {
        this.id = jwtClaims.getId();
        this.username = jwtClaims.getSubject();
        this.role = jwtClaims.get("role", String.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return Objects.equals(id, principal.id) && Objects.equals(username, principal.username) && Objects.equals(role, principal.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, role);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
