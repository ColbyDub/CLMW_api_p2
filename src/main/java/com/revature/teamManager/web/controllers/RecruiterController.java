package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Recruiter;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.RecruiterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Recruiter registerNewCoach(@RequestBody Recruiter recruiterCandidate) {
        return recruiterService.register(recruiterCandidate);
    }
}