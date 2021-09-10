package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.repos.CoachRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.web.dtos.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachService {

    private CoachRepository coachRepository;
    private PasswordUtils passwordUtils;

    @Autowired
    public CoachService(CoachRepository coachRepository, PasswordUtils passwordUtils){
        this.coachRepository = coachRepository;
        this.passwordUtils = passwordUtils;
    }

    public Principal login(String username, String password){

        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        Coach authCoach = coachRepository.findCoachByUsernameAndPassword(username,encryptedPassword);

        if(authCoach == null){
            throw new AuthenticationException("Invalid username/password combo");
        }

        return new Principal(authCoach);
    }

    public boolean isValid(Coach coach) {
        if (coach.getCoachName().trim().equals("") || coach.getUsername().trim().equals("") ||
                coach.getPassword().trim().equals("") || coach.getSport().trim().equals("") ||
                coach.getTeamName().trim().equals("") || coach.getCoachName() == null ||
                coach.getUsername() == null || coach.getPassword() == null || coach.getSport() == null ||
                coach.getTeamName() == null) {
            throw new InvalidRequestException("You must provide all necessary information");
        }
        if (coach.getPassword().length() < 8) {
            throw new InvalidRequestException("Your password must be at least 8 characters long");
        }
        if (coachRepository.findCoachByUsername(coach.getUsername()) != null) {
            throw new InvalidRequestException("That username is already taken");
        }
        return true;
    }

    public Coach register(Coach coach) {
        if (isValid(coach)) {
            coach.setPassword(passwordUtils.generateSecurePassword(coach.getPassword()));
            return coachRepository.save(coach);
        }
        return null;
    }

}
