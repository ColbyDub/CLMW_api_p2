package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.repos.CoachRepository;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

@Service
public class CoachService {

    private CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository){
        this.coachRepository = coachRepository;
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
            return coachRepository.save(coach);
        }
        return null;
    }

}
