package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.PlayerService;
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

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Coach registerNewCoach(@RequestBody Coach coachCandidate) {
        return coachService.register(coachCandidate);
    }

    @PutMapping(value = "/team", produces = "application/json", consumes = "application/json")
    public Coach addPlayer(@RequestBody Offer accepted) {
        playerService.removeOffer(accepted);
        Coach updatedCoach = coachService.addPlayer(accepted.getCoachUsername(), accepted.getPlayerUsername());
        return updatedCoach;
    }


    @PatchMapping(value = "/assign/{username}", produces = "application/json", consumes = "application/json")
    public void assignWorkout(@RequestBody String exercise, @PathVariable String username){
        for (String teamPlayer : coachService.getTeamPlayers(username)) {
            if(!playerService.addExercise(teamPlayer,exercise))
                System.out.println("Exercise ["+exercise+"] is already assigned to team member ["+teamPlayer+"]");
        }
        
    }
}