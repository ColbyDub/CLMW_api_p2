package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "recruiters")
public class Recruiter {

    private String name;
    private String username;
    private String password;

    public Recruiter(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Recruiter(){}

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

    @Override
    public String toString() {
        return "Recruiter{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recruiter recruiter = (Recruiter) o;
        return Objects.equals(name, recruiter.name) && username.equals(recruiter.username) && Objects.equals(password, recruiter.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, password);
    }
}
