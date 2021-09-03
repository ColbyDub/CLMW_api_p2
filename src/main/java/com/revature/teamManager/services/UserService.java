package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.repos.CoachRepository;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.web.dtos.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private CoachRepository coachRepo;
    private PlayerRepository playerRepo;

    @Autowired
    public UserService(CoachRepository coachRepo, PlayerRepository playerRepo, PasswordUtils passwordUtils){
        this.coachRepo = coachRepo;
        this.playerRepo = playerRepo;
    }


    public Principal login(String username, String password, String role){

        return null;
    }

    public boolean isUsernameAvailable(String username) { return (coachRepo.findCoachByUsername(username) == null); }


    public boolean isUserValid(Coach user) {
        if (user == null) return false;
        if (user.getCoachName() == null || user.getCoachName().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }
}
