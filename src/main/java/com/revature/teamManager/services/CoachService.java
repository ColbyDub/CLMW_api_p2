package com.revature.teamManager.services;

import com.revature.teamManager.data.repos.CoachRepository;
import org.springframework.stereotype.Service;

@Service
public class CoachService {

    private CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository){
        this.coachRepository = coachRepository;
    }

}
