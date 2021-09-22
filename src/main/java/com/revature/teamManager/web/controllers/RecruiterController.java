package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Pin;
import com.revature.teamManager.data.documents.Recruiter;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.RecruiterService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    //registers a new recruiter account
    @PostMapping(value = "{pin}", produces = "application/json", consumes = "application/json")
    public Recruiter registerNewRecruiter(@RequestBody Recruiter recruiterCandidate, @PathVariable String pin) {
        return recruiterService.register(recruiterCandidate, pin);
    }
}