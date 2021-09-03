package com.revature.teamManager.services;

import com.revature.teamManager.data.repos.CoachRepository;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private CoachRepository coachRepo;
    private PlayerRepository playerRepo;

    @Autowired
    public UserService(CoachRepository coachRepo, PlayerRepository playerRepo, RecruiterRepository recruiterRepo, PasswordUtils passwordUtils){
        this.coachRepo = coachRepo;
        this.playerRepo = playerRepo;
    }


    public boolean isUsernameAvailable(String username) {

        if (username == null || username.trim().equals("")) {
            throw new InvalidRequestException("Invalid email value provided!");
        }

        return (userRepo.findUserByUsername(username) == null);
    }

    public boolean isEmailAvailable(String email) {

        if (email == null || email.trim().equals("")) {
            throw new InvalidRequestException("Invalid email value provided!");
        }

        return (userRepo.findUserByEmail(email) == null);
    }

    public boolean isUserValid(AppUser user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }
}
