package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.web.dtos.AssignPositionRequest;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.CoachDTO;
import com.revature.teamManager.web.dtos.Offer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach")
public class CoachController {
    private final CoachService coachService;
    private final PlayerService playerService;

    public CoachController(CoachService coachService, PlayerService playerService) {
        this.coachService = coachService;
        this.playerService = playerService;
    }

    @GetMapping(value="{username}", produces = "application/json")
    public CoachDTO getCoach(@PathVariable String username) {
        Coach coach = coachService.getCoach(username);
        CoachDTO responseCoach = new CoachDTO(coach);
        return responseCoach;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Coach registerNewCoach(@RequestBody Coach coachCandidate) {
        return coachService.register(coachCandidate);
    }

    @PutMapping(value = "/positions", consumes = "application/json")
    public void assignPlayerPosition(@RequestBody AssignPositionRequest req) {
        coachService.assignPosition(req.getCoachUsername(), req.getPlayerUsername(), req.getPosition());
    }

    @PutMapping(value = "/team", produces = "application/json", consumes = "application/json")
    public Coach addPlayer(@RequestBody Offer accepted) {
        playerService.removeOffer(accepted);
        Coach updatedCoach = coachService.addPlayer(accepted.getCoachUsername(), accepted.getPlayerUsername());
        return updatedCoach;
    }
}