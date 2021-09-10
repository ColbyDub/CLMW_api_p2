package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.services.CoachService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach")
public class CoachController {
    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Coach registerNewCoach(@RequestBody Coach coachCandidate) {
        return coachService.register(coachCandidate);
    }
}