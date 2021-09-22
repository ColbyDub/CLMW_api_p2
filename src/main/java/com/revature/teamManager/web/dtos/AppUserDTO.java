package com.revature.teamManager.web.dtos;

//this is a generic user dto
public class AppUserDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;

//    public AppUserDTO(AppUser subject) {
//        this.id = subject.getId();
//        this.firstName = subject.getFirstName();
//        this.lastName = subject.getLastName();
//        this.email = subject.getEmail();
//        this.username = subject.getUsername();
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
